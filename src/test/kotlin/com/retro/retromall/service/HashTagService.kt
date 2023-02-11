package com.retro.retromall.service

import com.retro.retromall.hashtag.domain.HashTag
import com.retro.retromall.hashtag.domain.repository.HashTagRepository
import org.assertj.core.util.Arrays
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class HashTagService (
    @Autowired
    private val hashTagRepository: HashTagRepository
) {
    @BeforeEach
    fun 초기화() {
        var hashtag = HashTag(tag = "PS")
        hashTagRepository.save(hashtag)
    }
}