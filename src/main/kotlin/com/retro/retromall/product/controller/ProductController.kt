package com.retro.retromall.product.controller

import com.retro.common.annotation.MemberAuthentication
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.dto.ProductListResponse
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.dto.UpdateProductRequest
import com.retro.retromall.product.service.ProductReadService
import com.retro.retromall.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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

@Tag(name = "dev", description = "Retromall-dev api")
@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productReadService: ProductReadService,
    private val productService: ProductService
) {
    private val logger: Logger = LoggerFactory.getLogger(ProductController::class.java)

    @Operation(summary = "Product 조회", description = "Product 조회 컨트롤러")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Product 조회성공",
            content = [Content(schema = Schema(implementation = ProductResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Product 조회실패",
            content = [Content(schema = Schema(implementation = IllegalStateException::class))]
        )
    )
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
    fun productDelete(
        @MemberAuthentication memberAttributes: MemberAttributes,
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        productService.deleteProduct(memberAttributes, id)
        return ResponseEntity.ok().build()
    }
}