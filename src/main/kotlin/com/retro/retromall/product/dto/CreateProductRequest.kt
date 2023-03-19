package com.retro.retromall.product.dto

data class CreateProductRequest(
    val title: String,
    val content: String?,
    val amount: Int,
    val category: String,
    val thumbnail: String?,
    val images: Set<String>,
    val hashTags: Set<String>
)
