package com.retro.retromall.product.support

import com.retro.common.aop.CheckUserPermission
import com.retro.retromall.category.service.CategoryReadService
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.ProductEntity
import com.retro.retromall.product.domain.ProductHashTagEntity
import com.retro.retromall.product.domain.ProductImageEntity
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.ProductUpdateRequest
import com.retro.retromall.product.service.ProductHashTagService
import com.retro.retromall.product.service.ProductImageService
import com.retro.security.AuthenticationService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class ProductModifier(
    private val productRepository: ProductRepository,
    private val categoryReadService: CategoryReadService,
    private val productImageService: ProductImageService,
    private val productHashTagService: ProductHashTagService,
    private val productAuthenticationService: AuthenticationService,
) {
    @CheckUserPermission
    fun updateProduct(authenticationAttributes: AuthenticationAttributes, productId: Long, dto: ProductUpdateRequest): Long {
        val product =
            productRepository.findById(productId).orElseThrow { throw IllegalArgumentException("해당 상품을 찾을 수 없습니다.") }
//        if (!productAuthenticationService.validateUser(authenticationAttributes.id!!, product))
//            throw IllegalStateException("해당 상품을 수정할 권한이 없습니다.")

        modifyProduct(
            productEntity = product,
            title = dto.title ?: product.title,
            content = dto.content ?: product.content,
            amount = dto.amount,
            category = dto.category.let { categoryReadService.getCategory(it).name },
            hashTags = dto.hashTags.let { productHashTagService.createProductHashTags(product, it) },
            thumbnail = dto.thumbnail,
            images = dto.images.let { productImageService.createProductImages(it, product).toMutableSet()},
            addressId = dto.addressId ?: product.addressId
        )

        return productId
    }

    @CheckUserPermission
    fun deleteProduct(authenticationAttributes: AuthenticationAttributes, productId: Long) {
        val product =
            productRepository.findById(productId).orElseThrow { throw IllegalArgumentException("해당 상품을 찾을 수 없습니다.") }
//        if (!productAuthenticationService.validateUser(authenticationAttributes.id!!, product))
//            throw IllegalStateException("해당 상품을 삭제할 권한이 없습니다.")
        productRepository.delete(product)
    }

    private fun modifyProduct(
        productEntity: ProductEntity,
        title: String,
        content: String?,
        amount: Int,
        category: String,
        hashTags: MutableSet<ProductHashTagEntity>,
        thumbnail : String,
        images: MutableSet<ProductImageEntity>,
        addressId : Long
    ) {
        productEntity.title = title
        productEntity.content = content
        productEntity.amount = amount
        productEntity.category = category
        productEntity.hashTags.removeIf { hashTag -> !hashTags.contains(hashTag) }
        productEntity.hashTags.addAll(hashTags)
        productEntity.images.removeIf { image -> !images.contains(image) }
        productEntity.images.addAll(images)
        productEntity.thumbnail = thumbnail
        productEntity.addressId = addressId
        productEntity.modifiedAt = LocalDateTime.now()
    }
}