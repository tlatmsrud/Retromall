package com.retro.retromall.product.service

import com.retro.retromall.hashtag.service.HashTagService
import com.retro.retromall.product.domain.ProductEntity
import com.retro.retromall.product.domain.ProductHashTagEntity
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ProductHashTagService(
    private val hashTagService: HashTagService
) {
    fun createProductHashTags(productEntity: ProductEntity, hashTagRequest: Set<String>): MutableSet<ProductHashTagEntity> {
        val hashTags = hashTagService.findOrCreateHashtags(hashTagRequest)
        return hashTags.stream().map { ProductHashTagEntity(productEntity, it) }.collect(Collectors.toSet())
    }
}