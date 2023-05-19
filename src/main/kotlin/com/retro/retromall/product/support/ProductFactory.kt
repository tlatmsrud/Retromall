package com.retro.retromall.product.support

import com.retro.retromall.category.service.CategoryReadService
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductHashTag
import com.retro.retromall.product.domain.ProductImage
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
        val product = Product(
            title = dto.title,
            content = dto.content,
            amount = dto.amount,
            authorId = authenticationAttributes.id!!,
            category = categoryReadService.getCategory(dto.category).name,
            thumbnail = dto.thumbnail,
            addressId = dto.addressId
        )

        productRepository.save(product)
        addHashTags(product, productHashTagService.createProductHashTags(product, dto.hashTags))
        addImages(product, productImageService.createProductImages(dto.images, product))
        return product.id!!
    }

    private fun addHashTags(product: Product, hashtags: Set<ProductHashTag>) {
        product.hashTags.addAll(hashtags)
    }

    private fun addImages(product: Product, images: List<ProductImage>) {
        product.images.addAll(images)
        images.forEach { it.product = product }
    }
}