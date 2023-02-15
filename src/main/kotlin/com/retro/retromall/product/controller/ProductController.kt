package com.retro.retromall.product.controller

import com.retro.aop.annotation.MemberAuthentication
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.dto.UpdateProductRequest
import com.retro.retromall.product.service.ProductService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {
    private val logger: Logger = LoggerFactory.getLogger(ProductController::class.java)

    @PostMapping
    fun productAdd(
        @MemberAuthentication memberAttributes: MemberAttributes,
        @RequestBody createProductRequest: CreateProductRequest
    ): ResponseEntity<Long> {
        val id = productService.createProduct(memberAttributes, createProductRequest)
        return ResponseEntity.ok(id)
    }

    @PutMapping("/{id}")
    fun productUpdate(
        @MemberAuthentication memberAttributes: MemberAttributes,
        @PathVariable id: Long,
        @RequestBody updateProductRequest: UpdateProductRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }
}