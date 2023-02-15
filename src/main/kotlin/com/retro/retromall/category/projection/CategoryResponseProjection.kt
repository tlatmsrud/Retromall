package com.retro.retromall.category.projection

import com.retro.retromall.category.domain.Category

interface CategoryResponseProjection {
    val name: String
    val lowerCategoryList: MutableList<Category>
}