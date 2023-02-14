package com.retro.retromall.repository

import com.retro.retromall.category.domain.Category
import com.retro.retromall.category.domain.repository.CategoryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
class CategoryRepositoryTest(
    @Autowired
    private val categoryRepository: CategoryRepository,
) {
    @BeforeEach
    @Transactional
    fun init() {
        var psCategory = Category(isClassification = true, parent = null, name = "PS")
        categoryRepository.save(psCategory)

        var ps4Category = Category(isClassification = true, parent = psCategory, name = "PS4")
        var ps5Category = Category(isClassification = true, parent = psCategory, name = "PS5")
        psCategory.addLowerCategory(ps4Category)
        psCategory.addLowerCategory(ps5Category)
    }
}