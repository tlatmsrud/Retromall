package com.retro.retromall.product.service

import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductImage
import org.springframework.stereotype.Service

@Service
class ProductImageService(
) {
    fun createProductImages(images: Set<String>, product: Product): List<ProductImage> {
        return images.map { ProductImage(imageUrl = it, product) }
    }
}