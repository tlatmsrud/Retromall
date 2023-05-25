package com.retro.retromall.category.projection

import com.retro.retromall.category.domain.CategoryEntity

interface CategoryResponseProjection {
    val name: String
    val lowerCategoryListEntity: MutableList<CategoryEntity>
}