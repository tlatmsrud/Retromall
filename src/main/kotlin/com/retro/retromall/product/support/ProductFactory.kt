package com.retro.retromall.product.support

import com.retro.retromall.category.service.CategoryReadService
import com.retro.retromall.hashtag.service.HashTagService
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.service.MemberReadService
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductRepository
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.service.ProductImageService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProductFactory(
    private val productRepository: ProductRepository,
    private val memberReadService: MemberReadService,
    private val productImageService: ProductImageService,
    private val categoryReadService: CategoryReadService,
    private val hashTagService: HashTagService
) {
    @Transactional
    fun createProduct(memberAttributes: MemberAttributes, dto: CreateProductRequest): Long {
        val product = Product(
            content = dto.content,
            amount = dto.amount,
        )
        product.author = memberReadService.getMember(memberAttributes.id)
        product.category = categoryReadService.getCategory(dto.category)
        product.addHashTags(hashTagService.findOrCreateHashtags(dto.hashTags))
        product.addImages(productImageService.createProductImages(dto.images, product))

        productRepository.save(product)
        return product.id!!
    }
}