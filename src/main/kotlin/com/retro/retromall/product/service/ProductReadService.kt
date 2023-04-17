package com.retro.retromall.product.service

import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.dto.ProductListResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductReadService(
    private val productRepository: ProductRepository
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ProductReadService::class.java)
    }
    fun getProduct(authenticationAttributes: AuthenticationAttributes, productId: Long): ProductResponse {
        logger.info("{}", authenticationAttributes.id)
        return productRepository.selectProduct(productId, authenticationAttributes.id)
    }

    fun getProductList(category: String?, pageable: Pageable): ProductListResponse? {
        return productRepository.selectProductList(category, pageable)
    }
}