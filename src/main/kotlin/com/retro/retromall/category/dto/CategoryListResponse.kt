package com.retro.retromall.category.dto

data class CategoryListResponse(
    val data: List<Data>
) {
    data class Data(
        val category: String,
        val lowerCategories: List<Data>
    )
}