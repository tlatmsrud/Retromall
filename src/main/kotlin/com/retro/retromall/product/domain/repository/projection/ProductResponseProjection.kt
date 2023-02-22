package com.retro.retromall.product.domain.repository.projection

import java.time.LocalDateTime

interface ProductResponseProjection {
    val id: Long
    val content: String
    val amount: Int
    val author: AuthorResponseProjection
    val category: CategoryResponseProjection
    val hashTags: Set<HashTagResponseProjection>
    val images: Set<ProductImageResponseProjection>
    val createdAt: LocalDateTime
    val modifiedAt: LocalDateTime
}