package com.retro.retromall.product.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ProductResponse(
    @get:JsonProperty(value = "isAuthor")
    @param:JsonProperty(value = "isAuthor")
    val isAuthor: Boolean,
    val productId: Long,
    val title: String,
    val content: String?,
    val amount: Int,
    @get:JsonProperty(value = "author")
    @param:JsonProperty(value = "author")
    val author: String,
    val category: String,
    val likes: Long,
    val isLiked: Boolean,
    val hashTags: Set<String>,
    val images: Set<String>,
    val address: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
)