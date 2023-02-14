package com.retro.retromall.product.controller

import com.retro.aop.annotation.MemberAuthentication
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.dto.AddProductRequest
import com.retro.retromall.product.service.ProductService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/product")
class ProductController(
    private val productService: ProductService
) {
    private val logger: Logger = LoggerFactory.getLogger(ProductController::class.java)

    @PostMapping
    fun productAdd(
        @MemberAuthentication memberAttributes: MemberAttributes, @RequestBody addProductRequest: AddProductRequest
    ): ResponseEntity<Long> {
        val id = productService.createProduct(memberAttributes, addProductRequest)
        return ResponseEntity.ok(id)
    }
}