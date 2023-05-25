package com.retro.retromall.product.domain.repository

import com.retro.retromall.product.domain.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
}
