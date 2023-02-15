package com.retro.retromall.product.dto

import java.time.LocalDateTime

data class ProductResponse(
    val productId: Long,
    val content: String?,
    val amount: Int,
    var author: String?,
    var category: String,
    var hashTags: Set<String>,
    var images: Set<String>,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
)