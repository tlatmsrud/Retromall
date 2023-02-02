package com.retro.retromall.hashtag

import org.springframework.data.jpa.repository.JpaRepository

interface HashTagRepository : JpaRepository<HashTag, String> {
}