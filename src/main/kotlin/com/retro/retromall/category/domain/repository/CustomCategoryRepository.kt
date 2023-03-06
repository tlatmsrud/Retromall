package com.retro.retromall.category.domain.repository

import com.retro.retromall.category.dto.CategoryListResponse
import com.retro.retromall.category.dto.CategoryResponse

interface CustomCategoryRepository {
    fun selectCategoryListByRootCategory(root: String?): CategoryResponse
    fun selectCategories(): CategoryListResponse
}