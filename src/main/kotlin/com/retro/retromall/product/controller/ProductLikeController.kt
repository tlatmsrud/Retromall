package com.retro.retromall.product.controller

import com.retro.aop.annotation.MemberAuthentication
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.service.ProductLikeService
import org.springframework.web.bind.annotation.PatchMapping
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
        @MemberAuthentication authenticationAttributes: AuthenticationAttributes,
        @RequestParam("product_id") productId: Long
    ) {
        productLikeService.addProductLike(authenticationAttributes, productId)
    }

    @PatchMapping
    fun productLikeRemove(
        @MemberAuthentication authenticationAttributes: AuthenticationAttributes,
        @RequestParam("product_id") productId: Long
    ) {
        productLikeService.removeProductLike(authenticationAttributes, productId)
    }
}