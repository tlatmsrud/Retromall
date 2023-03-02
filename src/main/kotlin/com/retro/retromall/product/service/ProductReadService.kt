package com.retro.retromall.product.service

import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.dto.ProductListResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductReadService(
    private val productRepository: ProductRepository
) {
    fun getProduct(memberAttributes: MemberAttributes, productId: Long): ProductResponse {
        return productRepository.selectProduct(productId, memberAttributes.id)
    }

    fun getProductList(category: String?, pageable: Pageable): ProductListResponse? {
        return productRepository.selectProductList(category, pageable)
    }
}