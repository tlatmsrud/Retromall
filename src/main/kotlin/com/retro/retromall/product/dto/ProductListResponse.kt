package com.retro.retromall.product.dto

import org.springframework.data.domain.Slice
import java.time.LocalDateTime

data class ProductListResponse(
    val data: Slice<Data>
) {
    data class Data(
        val productId: Long,
        val author: String?,
        val content: String?,
        val amount: Int,
        val likes: Long,
        val thumbnail: String?,
        val createdAt: LocalDateTime,
        val modifiedAt: LocalDateTime
    )
}
