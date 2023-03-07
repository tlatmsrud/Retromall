package com.retro.retromall.category.dto

import java.io.Serializable

data class CategoryListResponse(
    val data: List<Data>
) : Serializable {
    data class Data(
        val category: String,
        val lowerCategories: List<Data>
    ) : Serializable
}