package com.retro.retromall.product.support

import com.retro.retromall.category.service.CategoryReadService
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.ProductEntity
import com.retro.retromall.product.domain.ProductHashTagEntity
import com.retro.retromall.product.domain.ProductImageEntity
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.service.ProductHashTagService
import com.retro.retromall.product.service.ProductImageService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class ProductFactory(
    private val productRepository: ProductRepository,
    private val categoryReadService: CategoryReadService,
    private val productImageService: ProductImageService,
    private val productHashTagService: ProductHashTagService,
) {
    fun createProduct(authenticationAttributes: AuthenticationAttributes, dto: CreateProductRequest): Long {
        val productEntity = ProductEntity(
            title = dto.title,
            content = dto.content,
            amount = dto.amount,
            authorId = authenticationAttributes.id!!,
            category = categoryReadService.getCategory(dto.category).name,
            thumbnail = dto.thumbnail,
            addressId = dto.addressId
        )

        productRepository.save(productEntity)
        addHashTags(productEntity, productHashTagService.createProductHashTags(productEntity, dto.hashTags))
        addImages(productEntity, productImageService.createProductImages(dto.images, productEntity))
        return productEntity.id!!
    }

    private fun addHashTags(productEntity: ProductEntity, hashtags: Set<ProductHashTagEntity>) {
        productEntity.hashTags.addAll(hashtags)
    }

    private fun addImages(productEntity: ProductEntity, images: List<ProductImageEntity>) {
        productEntity.images.addAll(images)
        images.forEach { it.productEntity = productEntity }
    }
}