package com.retro.retromall.service

import com.retro.retromall.category.domain.Category
import com.retro.retromall.category.domain.CategoryRepository
import com.retro.retromall.product.controller.dto.AddProductRequest
import com.retro.retromall.product.domain.ProductRepository
import com.retro.retromall.product.service.ProductService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ProductServiceTest(
    @Autowired
    private val productService: ProductService,

    @Autowired
    private val productRepository: ProductRepository,

    @Autowired
    private val categoryRepository: CategoryRepository,

) {
    @BeforeEach
    @Transactional
    fun init() {
        categoryRepository.save(Category(name = "PC", korName = "데스크탑"))
    }

    @Test
    @Transactional
    fun addProduct() {
        val dto = AddProductRequest(
            content = "",
            amount = 1000,
            category = "PC",
            imageList = mutableListOf(),
            hashTagList = mutableListOf("#PS", "#PC")
        )
        productService.addProduct(dto)

        val result = productRepository.findById(1L).orElseThrow { IllegalArgumentException("") }

        assertEquals(result.category.name, "PC")
        assertEquals(result.hashTags.size, 2)
    }
}