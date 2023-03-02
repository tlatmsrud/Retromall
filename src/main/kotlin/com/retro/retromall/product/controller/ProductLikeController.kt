package com.retro.retromall.product.controller

import com.retro.common.annotation.MemberAuthentication
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.service.ProductLikeService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products/like")
class ProductLikeController(
    private val productLikeService: ProductLikeService
) {
    @PostMapping
    fun productLikeAdd(
        @MemberAuthentication memberAttributes: MemberAttributes,
        @RequestParam("product_id") productId: Long
    ) {
        productLikeService.addProductLike(memberAttributes, productId)
    }
}