package com.retro.retromall.service

import com.retro.retromall.hashtag.domain.HashTag
import com.retro.retromall.hashtag.service.HashTagService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest(properties = ["spring.profiles.active=test"])
class HashTagServiceTest {
    @Autowired
    private lateinit var hashTagService: HashTagService

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private lateinit var testEntityManager: TestEntityManager

    @BeforeEach
    fun init() {
        val entityManagerFactory = entityManager.entityManagerFactory
        this.testEntityManager = TestEntityManager(entityManagerFactory)
    }

    @Test
    fun findOrCreateHashTagsWithNewTags() {
        //given
        val tagNames = setOf("tag1", "tag2", "tag3")
        val tags = tagNames.map { HashTag(it) }
        testEntityManager.persistAndFlush(tags)

        //when
        val result = hashTagService.findOrCreateHashtags(tagNames)

        //then
        assertThat(result).containsExactlyElementsOf(tags)


    }
}