package com.retro.retromall.hashtag.service

import com.retro.retromall.hashtag.domain.HashTag
import com.retro.retromall.hashtag.domain.repository.HashTagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors
import kotlin.streams.toList

@Service
@Transactional(readOnly = true)
class HashTagService(
    private val hashTagRepository: HashTagRepository
) {
    @Transactional
    fun findOrCreateHashtags(hashtagNames: List<String>): MutableList<HashTag> {
        val existingHashTags = hashTagRepository.findAllByNameIn(hashtagNames)
        val existingHashTagNames = existingHashTags.map { it.name }.toSet()

        val newHashTags = hashtagNames.stream().filter { !existingHashTagNames.contains(it) }
            .map { HashTag(it) }.toList()

        val allHashTags = mutableListOf<HashTag>()
        allHashTags.addAll(existingHashTags)
        allHashTags.addAll(newHashTags)

        if(newHashTags.isNotEmpty())
            hashTagRepository.saveAll(newHashTags)

        return allHashTags
    }
}