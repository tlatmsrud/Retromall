package com.retro.retromall.category.domain.repository

import com.retro.retromall.category.domain.Category
import com.retro.retromall.category.dto.CategoryResponse
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, String>, CustomCategoryRepository {
    @EntityGraph(attributePaths = ["lowerCategoryList"])
    fun findAllProjectedByParentIsNull(): CategoryResponse
}