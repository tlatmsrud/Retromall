package com.retro.retromall.category.domain.repository

import com.retro.retromall.category.dto.CategoryListResponse

interface CustomCategoryRepository {
    fun selectCategoryListByRootCategory(root: String): CategoryListResponse
}