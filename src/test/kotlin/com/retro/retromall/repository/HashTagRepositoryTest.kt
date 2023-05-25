package com.retro.retromall.repository

import com.retro.retromall.hashtag.domain.HashTagEntity
import com.retro.retromall.hashtag.domain.repository.HashTagRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class HashTagRepositoryTest(
    @Autowired
    private val hashTagRepository: HashTagRepository
) {
    @BeforeEach
    fun init() {
        val hashtag = HashTagEntity(name = "PS")
        hashTagRepository.save(hashtag)
    }
}