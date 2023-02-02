package com.retro.retromall.category

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, Long> {
}