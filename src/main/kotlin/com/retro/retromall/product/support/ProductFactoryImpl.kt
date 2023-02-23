package com.retro.retromall.product.support

import com.retro.retromall.category.service.CategoryReadService
import com.retro.retromall.hashtag.service.HashTagService
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.service.MemberReadService
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.service.ProductImageService
import org.springframework.stereotype.Component

@Component
class ProductFactoryImpl(
    private val productRepository: ProductRepository,
    private val memberReadService: MemberReadService,
    private val productImageService: ProductImageService,
    private val categoryReadService: CategoryReadService,
    private val hashTagService: HashTagService
) : ProductFactory {
    override fun createProduct(memberAttributes: MemberAttributes, dto: CreateProductRequest): Long {
        val product = Product(
            content = dto.content,
            amount = dto.amount,
            thumbnail = dto.thumbnail
        )
        product.author = memberReadService.getMember(memberAttributes.id)
        product.category = categoryReadService.getCategory(dto.category)
        product.addHashTags(hashTagService.findOrCreateHashtags(dto.hashTags))
        product.addImages(productImageService.createProductImages(dto.images, product))

        productRepository.save(product)
        return product.id!!
    }
}