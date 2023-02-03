package com.retro.retromall.product.controller.dto

data class AddProductRequest(
    val content: String,
    val amount: Int,
    val category: String
)
