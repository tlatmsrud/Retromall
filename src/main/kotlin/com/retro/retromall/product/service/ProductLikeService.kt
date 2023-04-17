package com.retro.retromall.product.service

import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductLikeService(
    private val productRepository: ProductRepository,
) {
    fun addProductLike(authenticationAttributes: AuthenticationAttributes, productId: Long) {
        val product =
            productRepository.findById(productId).orElseThrow { throw IllegalArgumentException("해당 상품을 찾을 수 없습니다.") }
        val productLike = productRepository.selectProductLike(productId, authenticationAttributes.id!!)
        product.addLikes(authenticationAttributes.id, productLike)
    }

    fun removeProductLike(authenticationAttributes: AuthenticationAttributes, productId: Long) {
        val product =
            productRepository.findById(productId).orElseThrow { throw IllegalArgumentException("해당 상품을 찾을 수 없습니다.") }
        val productLike = productRepository.selectProductLike(productId, authenticationAttributes.id!!)
        product.removeLikes(productLike)
    }
}