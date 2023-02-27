package com.retro.retromall.product.domain.repository

import com.retro.retromall.product.dto.ProductListResponse
import com.retro.retromall.product.dto.ProductResponse
import org.springframework.data.domain.Pageable

interface ProductRepositoryCustom {
    fun selectProduct(productId: Long): ProductResponse
    fun selectProductList(category: String?, pageable: Pageable): ProductListResponse
}