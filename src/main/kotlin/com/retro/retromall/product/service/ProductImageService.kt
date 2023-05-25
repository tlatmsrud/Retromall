package com.retro.retromall.product.service

import com.retro.retromall.product.domain.ProductEntity
import com.retro.retromall.product.domain.ProductImageEntity
import org.springframework.stereotype.Service

@Service
class ProductImageService(
) {
    fun createProductImages(images: Set<String>, productEntity: ProductEntity): List<ProductImageEntity> {
        return images.map { ProductImageEntity(image = it, productEntity = productEntity) }
    }
}