package com.retro.retromall.product.service

import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductReadService(
    private val productRepository: ProductRepository
) {
    fun getProduct(productId: Long): Product {
        return productRepository.findById(productId).orElseThrow { throw IllegalArgumentException("해당 상품을 찾을 수 없습니다.") }
    }
}