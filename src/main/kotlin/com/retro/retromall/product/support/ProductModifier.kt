package com.retro.retromall.product.support

import com.retro.exception.ProductException
import com.retro.retromall.authorization.enums.Permission
import com.retro.retromall.category.service.CategoryReadService
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.ProductEntity
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.ProductUpdateRequest
import com.retro.retromall.product.service.ProductAuthorizationService
import com.retro.retromall.product.service.ProductHashTagService
import com.retro.retromall.product.service.ProductImageService
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
    private val productAuthorizationService: ProductAuthorizationService
) {
    fun updateProduct(
        authenticationAttributes: AuthenticationAttributes,
        productId: Long,
        dto: ProductUpdateRequest
    ): Long {
        val product = getProductById(productId)
        checkProductPermission(product, authenticationAttributes, Permission.UPDATE_PRODUCT)
        updateProductAttributes(product, dto)
        return productId
    }

    fun deleteProduct(authenticationAttributes: AuthenticationAttributes, productId: Long) {
        val product = getProductById(productId)
        checkProductPermission(product, authenticationAttributes, Permission.DELETE_PRODUCT)
        productRepository.delete(product)
    }

    private fun getProductById(productId: Long): ProductEntity {
        return productRepository.findById(productId)
            .orElseThrow { throw ProductException("해당 상품을 찾을 수 없습니다.") }
    }

    private fun checkProductPermission(
        product: ProductEntity,
        authenticationAttributes: AuthenticationAttributes,
        type: Permission
    ) {
        productAuthorizationService.checkPermission(product, authenticationAttributes, type)
    }

    private fun updateProductAttributes(product: ProductEntity, dto: ProductUpdateRequest) {
        product.apply {
            title = dto.title ?: title
            content = dto.content ?: content
            amount = dto.amount
            category = categoryReadService.getCategory(dto.category).name
            hashTags.addAll(dto.hashTags.let { productHashTagService.createProductHashTags(product, it) })
            thumbnail = dto.thumbnail
            images.addAll(dto.images.let { productImageService.createProductImages(it, product) })
            addressId = dto.addressId ?: addressId
            modifiedAt = LocalDateTime.now()
        }
    }
}
