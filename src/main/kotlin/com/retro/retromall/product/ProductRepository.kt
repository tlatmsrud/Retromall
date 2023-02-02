package com.retro.retromall.product

import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<Product, Long> {
}