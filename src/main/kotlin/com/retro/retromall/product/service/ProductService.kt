package com.retro.retromall.product.service

import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.domain.ProductRepository
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.dto.UpdateProductRequest
import com.retro.retromall.product.support.ProductFactory
import com.retro.retromall.product.support.ProductModifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productFactory: ProductFactory,
    private val productModifier: ProductModifier
) {
    @Transactional
    fun createProduct(memberAttributes: MemberAttributes, dto: CreateProductRequest): Long {
        return productFactory.createProduct(memberAttributes, dto)
    }

    @Transactional
    fun updateProduct(memberAttributes: MemberAttributes, productId: Long, dto: UpdateProductRequest): Long {
        return productModifier.updateProduct(memberAttributes, productId, dto)
    }

    @Transactional
    fun deleteProduct(memberAttributes: MemberAttributes, productId: Long) {
        productModifier.deleteProduct(memberAttributes, productId)
    }
}