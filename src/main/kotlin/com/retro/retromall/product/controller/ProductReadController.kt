package com.retro.retromall.product.controller

import com.retro.retromall.product.dto.ProductListResponse
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.service.ProductReadService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Product", description = "Product Read API")
@RestController
@RequestMapping("/api/v1/products")
class ProductReadController(
    private val productReadService: ProductReadService
) {
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

    @Operation(summary = "Product 목록 조회", description = "Product 목록 조회 컨트롤러")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Product 목록 조회성공",
            content = [Content(schema = Schema(implementation = ProductResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Product 목록 조회실패",
            content = [Content(schema = Schema(implementation = IllegalStateException::class))]
        )
    )
    @GetMapping
    fun productList(@Parameter(required = false) @RequestParam("category") category: String?): ResponseEntity<ProductListResponse> {
        return ResponseEntity.ok(productReadService.getProductList(category))
    }
}