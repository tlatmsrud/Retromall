package com.retro.retromall.repository

import com.retro.retromall.category.Category
import com.retro.retromall.category.CategoryRepository
import com.retro.retromall.product.Product
import com.retro.retromall.product.ProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
class ProductRepositoryTest(
    @Autowired
    private var productRepository: ProductRepository,

    @Autowired
    private var categoryRepository: CategoryRepository
) {
    @BeforeEach
    fun init() {
        var category: Category = createCategory()
        val product =
            Product(content = "content", amount = 12000, category)

        val result = productRepository.save(product)
    }

    private fun createCategory(): Category {
        return categoryRepository.save(Category(category = "PC", korValue = "데스크탑"))
    }

    @Test
    fun addImage() {

    }
}