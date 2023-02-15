package com.retro.retromall.product.service

import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.dto.UpdateProductRequest
import com.retro.retromall.product.support.ProductFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    private val productFactory: ProductFactory
) {
    fun createProduct(memberAttributes: MemberAttributes, dto: CreateProductRequest): Long {
        return productFactory.createProduct(memberAttributes, dto)
    }

    @Transactional
    fun updateProduct(memberAttributes: MemberAttributes, dto: UpdateProductRequest) {

    }
}