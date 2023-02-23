package com.retro.retromall.product.dto

import java.time.LocalDateTime

data class ProductListResponse(
    val data: List<Data>
) {
    data class Data(
        val productId: Long,
        val author: String?,
        val content: String?,
        val amount: Int,
        val thumbnail: String?,
        val createdAt: LocalDateTime,
        val modifiedAt: LocalDateTime
    )
}
