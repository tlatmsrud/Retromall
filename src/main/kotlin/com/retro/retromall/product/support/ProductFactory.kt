package com.retro.retromall.product.support

import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.dto.CreateProductRequest

interface ProductFactory {
    fun createProduct(memberAttributes: MemberAttributes, dto: CreateProductRequest): Long
}