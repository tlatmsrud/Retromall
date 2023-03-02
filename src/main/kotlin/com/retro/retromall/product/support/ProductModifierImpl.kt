package com.retro.retromall.product.support

import com.retro.retromall.category.service.CategoryReadService
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.service.MemberReadService
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.UpdateProductRequest
import com.retro.retromall.product.service.ProductHashTagService
import com.retro.retromall.product.service.ProductImageService
import com.retro.security.AuthenticationService
import org.springframework.stereotype.Component

@Component
class ProductModifierImpl(
    private val productRepository: ProductRepository,
    private val memberReadService: MemberReadService,
    private val categoryReadService: CategoryReadService,
    private val productImageService: ProductImageService,
    private val productHashTagService: ProductHashTagService,
    private val authenticationService: AuthenticationService

) : ProductModifier {
    override fun updateProduct(memberAttributes: MemberAttributes, productId: Long, dto: UpdateProductRequest): Long {
        val member = memberReadService.getMember(memberAttributes.id!!)
        val product =
            productRepository.findById(productId).orElseThrow { throw IllegalArgumentException("해당 상품을 찾을 수 없습니다.") }
        authenticationService.validateUser(member, product)
        product.content = dto.content ?: product.content
        product.amount = dto.amount
        dto.category.let {
            product.category = categoryReadService.getCategory(it).name
        }
        dto.hashTags.let { product.hashTags = productHashTagService.createProductHashTags(product, it) }
        dto.images.let { product.images = productImageService.createProductImages(it, product).toMutableSet() }

        return productRepository.save(product).id!!
    }

    override fun deleteProduct(memberAttributes: MemberAttributes, productId: Long) {
        val member = memberReadService.getMember(memberAttributes.id!!)
        val product =
            productRepository.findById(productId).orElseThrow { throw IllegalArgumentException("해당 상품을 찾을 수 없습니다.") }
        authenticationService.validateUser(member, product)
        productRepository.delete(product)
    }
}