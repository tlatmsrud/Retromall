package com.retro.retromall.category.domain.repository

import com.retro.retromall.category.domain.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<CategoryEntity, String>, CustomCategoryRepository {
//    @EntityGraph(attributePaths = ["lowerCategoryList"])
//    fun findAllProjectedByParentIsNull(): CategoryResponse
}