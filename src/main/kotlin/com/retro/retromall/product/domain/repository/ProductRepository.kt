package com.retro.retromall.product.domain.repository

import com.retro.retromall.product.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>, ProductRepositoryCustom {
}
