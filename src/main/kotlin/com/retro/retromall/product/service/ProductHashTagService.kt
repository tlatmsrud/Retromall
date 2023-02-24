package com.retro.retromall.product.service

import com.retro.retromall.hashtag.service.HashTagService
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductHashTag
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ProductHashTagService(
    private val hashTagService: HashTagService
) {
    fun createProductHashTags(product: Product, hashTagRequest: Set<String>): MutableSet<ProductHashTag> {
        val hashTags = hashTagService.findOrCreateHashtags(hashTagRequest)
        return hashTags.stream().map { ProductHashTag(product, it) }.collect(Collectors.toSet())
    }
}