package com.retro.retromall.service

import com.retro.retromall.hashtag.domain.HashTag
import com.retro.retromall.hashtag.domain.repository.HashTagRepository
import com.retro.retromall.hashtag.service.HashTagService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

import org.assertj.core.api.Assertions.assertThat
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.never

@ExtendWith(MockitoExtension::class)
class HashTagServiceMockTest {
    @Mock
    private lateinit var hashTagRepository: HashTagRepository

    @InjectMocks
    private lateinit var hashTagService: HashTagService

    @Test
    fun findOrCreateHashTagsWithNewTags() {
        //given
        val tagNames = setOf("tag1", "tag2", "tag3", "tag4")
        val existingTags = setOf(HashTag("tag1"), HashTag("tag2"))
        Mockito.`when`(hashTagRepository.findAllByNameIn(tagNames)).thenReturn(existingTags)

        //when
        val result = hashTagService.findOrCreateHashtags(tagNames)

        //then
        Mockito.verify(hashTagRepository).findAllByNameIn(tagNames)
        Mockito.verify(hashTagRepository).saveAll(anyList())

        assertThat(result.size).isEqualTo(4)
        assertThat(result).containsExactly(HashTag("tag1"), HashTag("tag2"), HashTag("tag3"), HashTag("tag4"))
    }

    @Test
    fun findOrCreateHashTagsExistingTags() {
        //given
        val tagNames = setOf("tag1", "tag2", "tag3", "tag4")
        val existingTags = setOf(HashTag("tag1"), HashTag("tag2"), HashTag("tag3"), HashTag("tag4"))
        Mockito.`when`(hashTagRepository.findAllByNameIn(tagNames)).thenReturn(existingTags)

        //when
        val result = hashTagService.findOrCreateHashtags(tagNames)

        //then
        Mockito.verify(hashTagRepository).findAllByNameIn(tagNames)
        Mockito.verify(hashTagRepository, never()).saveAll(anyList())

        assertThat(result.size).isEqualTo(4)
        assertThat(result).containsExactly(HashTag("tag1"), HashTag("tag2"), HashTag("tag3"), HashTag("tag4"))
    }
}