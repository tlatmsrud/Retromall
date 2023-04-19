package com.retro.retromall.product.service

import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.dto.ProductUpdateRequest
import com.retro.retromall.product.support.ProductFactory
import com.retro.retromall.product.support.ProductModifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

@Service
@Validated
class ProductService(
    private val productFactory: ProductFactory,
    private val productModifier: ProductModifier
) {
    @Transactional
    fun createProduct(authenticationAttributes: AuthenticationAttributes, @Valid dto: CreateProductRequest): Long {
        return productFactory.createProduct(authenticationAttributes, dto)
    }

    @Transactional
    fun updateProduct(authenticationAttributes: AuthenticationAttributes, productId: Long, dto: ProductUpdateRequest): Long {
        return productModifier.updateProduct(authenticationAttributes, productId, dto)
    }

    @Transactional
    fun deleteProduct(authenticationAttributes: AuthenticationAttributes, productId: Long) {
        productModifier.deleteProduct(authenticationAttributes, productId)
    }
}