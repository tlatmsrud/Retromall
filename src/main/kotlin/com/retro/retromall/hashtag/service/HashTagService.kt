package com.retro.retromall.hashtag.service

import com.retro.retromall.hashtag.domain.HashTag
import com.retro.retromall.hashtag.domain.repository.HashTagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class HashTagService(
    private val hashTagRepository: HashTagRepository
) {
    @Transactional
    fun addHashTags(hashTags: MutableList<String>) {
        val tags = hashTags.stream().map { tag -> HashTag(tag = tag) }.collect(Collectors.toList())
        hashTagRepository.saveAll(tags)
    }
}