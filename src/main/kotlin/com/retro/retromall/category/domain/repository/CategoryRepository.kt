package com.retro.retromall.category.domain.repository

import com.retro.retromall.category.domain.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, String>, CustomCategoryRepository {
}