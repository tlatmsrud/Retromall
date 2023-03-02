package com.retro.retromall.product.dto

import java.time.LocalDateTime

data class ProductResponse(
    val productId: Long,
    val content: String?,
    val amount: Int,
    val author: String,
    val category: String,
    val likes: Long,
    val isLiked: Boolean,
    val hashTags: Set<String>,
    val images: Set<String>,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
)