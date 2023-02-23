package com.retro.retromall.product.domain.repository

import com.retro.retromall.product.dto.ProductListResponse

interface ProductRepositoryCustom {
    fun selectProductList(): ProductListResponse
}