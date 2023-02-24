package com.retro.retromall.product.support

import com.retro.retromall.category.service.CategoryReadService
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.service.ProductHashTagService
import com.retro.retromall.product.service.ProductImageService
import org.springframework.stereotype.Component

@Component
class ProductFactoryImpl(
    private val productRepository: ProductRepository,
    private val categoryReadService: CategoryReadService,
    private val productImageService: ProductImageService,
    private val productHashTagService: ProductHashTagService
) : ProductFactory {
    override fun createProduct(memberAttributes: MemberAttributes, dto: CreateProductRequest): Long {
        val product = Product(
            content = dto.content,
            amount = dto.amount,
            authorId = memberAttributes.id,
            category = categoryReadService.getCategory(dto.category).name,
            thumbnail = dto.thumbnail
        )
        product.addHashTags(productHashTagService.createProductHashTags(product, dto.hashTags))
        product.addImages(productImageService.createProductImages(dto.images, product))

        productRepository.save(product)
        return product.id!!
    }
}