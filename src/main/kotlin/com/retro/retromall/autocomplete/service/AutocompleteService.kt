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
    @Value("\${autocomplete.key}") private val key : String
){
    fun getAutocomplete(searchWord : String) : AutocompleteResponse {
        val zSetOperations = redisTemplate.opsForZSet()
        var autoCompleteList  = emptyList<String>()

        zSetOperations.rank(key, searchWord)?.let {

            val rangeList = zSetOperations.range(key, it, -1) as Set<String>

            autoCompleteList =  rangeList.stream()
                .filter { value -> value.endsWith(suffix) && value.contains(searchWord)}
                .map { value -> StringUtils.removeEnd(value,suffix) }
                .limit(limit)
                .toList()
        }
        return AutocompleteResponse(autoCompleteList)
    }
}