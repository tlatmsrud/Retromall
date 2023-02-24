package com.retro.retromall.hashtag.service

import com.retro.retromall.hashtag.domain.HashTag
import com.retro.retromall.hashtag.domain.repository.HashTagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.streams.toList

@Service
@Transactional(readOnly = true)
class HashTagService(
    private val hashTagRepository: HashTagRepository
) {
    @Transactional
    fun findOrCreateHashtags(hashtagNames: Set<String>): Set<String> {
        val existingHashTags = hashTagRepository.findAllByNameIn(hashtagNames)
        val existingHashTagNames = existingHashTags.map { it.name }.toSet()

        val newHashTags = hashtagNames.stream().filter { !existingHashTagNames.contains(it) }
            .map { HashTag(it) }.toList()

        val allHashTags = mutableSetOf<String>()
        allHashTags.addAll(existingHashTags.stream().map { it.name }.toList())
        allHashTags.addAll(newHashTags.stream().map { it.name }.toList())

        if (newHashTags.isNotEmpty())
            hashTagRepository.saveAll(newHashTags)

        return allHashTags
    }
}