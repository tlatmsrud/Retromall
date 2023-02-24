package com.retro.retromall.product.service

import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.dto.ProductListResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductReadService(
    private val productRepository: ProductRepository
) {
    fun getProduct(productId: Long): ProductResponse {
        return productRepository.selectProduct(productId)
    }

    fun getProductList(category: String?): ProductListResponse? {
//        return productRepository.selectProductList()
        return null
    }
}