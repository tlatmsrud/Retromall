package com.retro.retromall.category.domain

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, String> {
}