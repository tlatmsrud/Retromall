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
        val projection =
            productRepository.findProjectedById(productId) ?: throw IllegalArgumentException("해당 상품을 찾을 수 없습니다.")
        val authorName = projection.author.nickname
        val categoryName = projection.category.name
        val hashTagNames = projection.hashTags.map { it.name }.toSet()
        val imageUrls = projection.images.map { it.imageUrl }.toSet()

        return ProductResponse(
            productId = projection.id,
            content = projection.content,
            amount = projection.amount,
            author = authorName,
            category = categoryName,
            hashTags = hashTagNames,
            images = imageUrls,
            createdAt = projection.createdAt,
            modifiedAt = projection.modifiedAt
        )
    }

    fun getProductList(category: String?): ProductListResponse {
        return productRepository.selectProductList()
    }
}