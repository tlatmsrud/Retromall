package com.retro.retromall.product.support

import com.retro.retromall.category.service.CategoryReadService
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.ProductUpdateRequest
import com.retro.retromall.product.service.ProductHashTagService
import com.retro.retromall.product.service.ProductImageService
import com.retro.security.AuthenticationService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class ProductModifierImpl(
    private val productRepository: ProductRepository,
    private val memberRepository: MemberRepository,
    private val categoryReadService: CategoryReadService,
    private val productImageService: ProductImageService,
    private val productHashTagService: ProductHashTagService,
    private val authenticationService: AuthenticationService

) : ProductModifier {
    override fun updateProduct(memberAttributes: MemberAttributes, productId: Long, dto: ProductUpdateRequest): Long {
        val member = memberRepository.findById(memberAttributes.id!!)
            .orElseThrow { throw IllegalArgumentException("사용자를 찾을 수 없습니다.") }
        val product =
            productRepository.findById(productId).orElseThrow { throw IllegalArgumentException("해당 상품을 찾을 수 없습니다.") }
        authenticationService.validateUser(member, product)
        product.modifyProduct(
            content = dto.content ?: product.content,
            amount = dto.amount,
            category = dto.category.let { categoryReadService.getCategory(it).name },
            hashTags = dto.hashTags.let { productHashTagService.createProductHashTags(product, it) },
            images = dto.images.let { productImageService.createProductImages(it, product).toMutableSet() }
        )

        return productId
    }

    override fun deleteProduct(memberAttributes: MemberAttributes, productId: Long) {
        val member = memberRepository.findById(memberAttributes.id!!)
            .orElseThrow { throw IllegalArgumentException("사용자를 찾을 수 없습니다.") }
        val product =
            productRepository.findById(productId).orElseThrow { throw IllegalArgumentException("해당 상품을 찾을 수 없습니다.") }
        authenticationService.validateUser(member, product)
        productRepository.delete(product)
    }
}