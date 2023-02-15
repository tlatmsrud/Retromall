package com.retro.retromall.product.controller

import com.retro.common.annotation.MemberAuthentication
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.dto.ProductListResponse
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.dto.UpdateProductRequest
import com.retro.retromall.product.service.ProductReadService
import com.retro.retromall.product.service.ProductService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productReadService: ProductReadService,
    private val productService: ProductService
) {
    private val logger: Logger = LoggerFactory.getLogger(ProductController::class.java)

    @GetMapping("/{id}")
    fun product(@PathVariable id: Long): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(productReadService.getProduct(id))
    }

    @GetMapping
    fun productList(): ResponseEntity<ProductListResponse> {
        return ResponseEntity.ok(productReadService.getProductList())
    }

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
        productService.updateProduct(memberAttributes, id, updateProductRequest)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun productDelete(@MemberAuthentication memberAttributes: MemberAttributes, @PathVariable id: Long): ResponseEntity<Unit> {
        productService.deleteProduct(memberAttributes, id)
        return ResponseEntity.ok().build()
    }
}