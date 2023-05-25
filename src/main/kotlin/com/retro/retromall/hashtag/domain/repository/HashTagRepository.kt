package com.retro.retromall.hashtag.domain.repository

import com.retro.retromall.hashtag.domain.HashTagEntity
import org.springframework.data.jpa.repository.JpaRepository

interface HashTagRepository : JpaRepository<HashTagEntity, String> {
    fun findAllByNameIn(hashtags: Set<String>): Set<HashTagEntity>
}