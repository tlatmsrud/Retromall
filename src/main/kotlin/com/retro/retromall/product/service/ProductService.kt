package com.retro.retromall.product.service

import com.retro.retromall.category.domain.CategoryRepository
import com.retro.retromall.product.controller.dto.AddProductRequest
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {
    @Transactional
    fun addProduct(dto: AddProductRequest) {
        val category = categoryRepository.findById(dto.category)
            .orElseThrow { IllegalArgumentException("존재하지 않는 카테고리입니다.") }
        val product = Product(content = dto.content, amount = dto.amount, category = category)
        productRepository.save(product)
    }
}