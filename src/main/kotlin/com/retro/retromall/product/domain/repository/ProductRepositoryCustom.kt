package com.retro.retromall.product.domain.repository

import com.retro.retromall.product.domain.ProductLikeEntity
import com.retro.retromall.product.dto.ProductListResponse
import com.retro.retromall.product.dto.ProductResponse
import org.springframework.data.domain.Pageable

interface ProductRepositoryCustom {
    fun selectProduct(productId: Long, memberId: Long?): ProductResponse
    fun selectProductList(category: String?, pageable: Pageable): ProductListResponse
    fun selectProductLike(productId: Long, memberId: Long): ProductLikeEntity?
    fun selectProductListBySearchWord(searchWord : String, pageable: Pageable) : ProductListResponse
}