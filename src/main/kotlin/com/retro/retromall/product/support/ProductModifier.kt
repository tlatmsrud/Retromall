package com.retro.retromall.product.support

import com.retro.retromall.product.dto.UpdateProductRequest

interface ProductModifier {
    fun updateProduct(productId: Long, dto: UpdateProductRequest): Long
    fun deleteProduct(productId: Long)
}