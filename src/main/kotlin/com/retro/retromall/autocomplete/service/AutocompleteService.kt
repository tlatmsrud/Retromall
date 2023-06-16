package com.retro.retromall.autocomplete.service


import com.retro.retromall.autocomplete.dto.AutocompleteResponse
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class AutocompleteService (
    private val redisTemplate : RedisTemplate<String, String>,
    @Value("\${autocomplete.limit}") private val limit: Long,
    @Value("\${autocomplete.suffix}") private val suffix : String,
    @Value("\${autocomplete.key}") private val key : String,
    @Value("\${autocomplete.score-key}") private val scoreKey : String
){
    fun getAutocomplete(searchWord : String) : AutocompleteResponse {

        val autocompleteList = getAutoCompleteListFromRedis(searchWord)
        return sortAutocompleteListByScore(autocompleteList)
    }

    fun addAutocomplete(searchWord : String ){

        val zSetOperations = redisTemplate.opsForZSet()
        zSetOperations.incrementScore(scoreKey, searchWord, 1.0)

        zSetOperations.score(key, searchWord)?:let {
            for(i in 1..searchWord.length){
                zSetOperations.add(key, searchWord.substring(0,i),0.0)
            }
            zSetOperations.add(key, searchWord+suffix,0.0)
        }

    }

    fun getAutoCompleteListFromRedis(searchWord : String) : List<String> {

        val zSetOperations = redisTemplate.opsForZSet()
        var autocompleteList = emptyList<String>()

        zSetOperations.rank(key, searchWord)?.let {
            val rangeList = zSetOperations.range(key, it, it + 1000) as Set<String>
            autocompleteList = rangeList.stream()
                .filter { value -> value.endsWith(suffix) && value.startsWith(searchWord) }
                .map { value -> StringUtils.removeEnd(value, suffix) }
                .limit(limit)
                .toList()
        }

        return autocompleteList
    }

    fun sortAutocompleteListByScore(autocompleteList : List<String>) : AutocompleteResponse{
        val zSetOperations = redisTemplate.opsForZSet()

        val list = arrayListOf<AutocompleteResponse.Data>()
        autocompleteList.forEach{word ->
                zSetOperations.score(scoreKey, word)?.let {
                    list.add(AutocompleteResponse.Data(word, it))
                }
        }
        list.sortByDescending { it.score }
        return AutocompleteResponse(list)

    }
}