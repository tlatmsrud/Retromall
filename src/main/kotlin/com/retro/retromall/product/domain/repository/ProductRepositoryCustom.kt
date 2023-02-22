package com.retro.retromall.product.domain.repository

import com.retro.retromall.product.dto.ProductResponse

interface ProductRepositoryCustom {
    fun selectAllProductResponseByCategoryName(categoryName: String?): List<ProductResponse>
}