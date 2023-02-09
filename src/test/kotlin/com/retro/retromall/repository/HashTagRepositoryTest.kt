package com.retro.retromall.repository

import com.retro.retromall.hashtag.domain.HashTag
import com.retro.retromall.hashtag.domain.repository.HashTagRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

@DataJpaTest
class HashTagRepositoryTest(
    @Autowired
    private val hashTagRepository: HashTagRepository
) {
    @BeforeEach
    fun init() {
        val hashtag = HashTag(tag = "PS")
        hashTagRepository.save(hashtag)
    }
}