package com.retro.retromall.product.dto

data class UpdateProductRequest (
    val content: String?,
    val amount: Int,
    val category: String,
    val images: Set<String>,
    val hashTags: Set<String>
)