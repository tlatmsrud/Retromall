package com.retro.retromall.product.domain.repository

import com.retro.retromall.product.dto.ProductListResponse
import org.springframework.data.domain.Pageable

interface ProductRepositoryCustom {
    fun selectProductList(pageable: Pageable): ProductListResponse
}