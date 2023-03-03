package com.retro.retromall.product.support

import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.dto.ProductUpdateRequest

interface ProductModifier {
    fun updateProduct(memberAttributes: MemberAttributes, productId: Long, dto: ProductUpdateRequest): Long
    fun deleteProduct(memberAttributes: MemberAttributes, productId: Long)
}