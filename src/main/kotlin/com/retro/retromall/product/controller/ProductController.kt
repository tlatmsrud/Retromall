package com.retro.retromall.product.controller

import com.retro.common.annotation.MemberAuthentication
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.dto.UpdateProductRequest
import com.retro.retromall.product.service.ProductReadService
import com.retro.retromall.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Product", description = "Product API")
@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productReadService: ProductReadService,
    private val productService: ProductService
) {
    private val logger: Logger = LoggerFactory.getLogger(ProductController::class.java)

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Product 생성", description = "Product 생성 컨트롤러")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Product 생성성공",
            content = [Content(schema = Schema(implementation = ProductResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Product 생성실패",
            content = [Content(schema = Schema(implementation = IllegalStateException::class))]
        )
    )
    @PostMapping
    fun productAdd(
        @Parameter(hidden = true) @MemberAuthentication memberAttributes: MemberAttributes,
        @Parameter(
            description = "Product 생성 데이터",
            schema = Schema(implementation = CreateProductRequest::class)
        ) @RequestBody createProductRequest: CreateProductRequest
    ): ResponseEntity<Long> {
        val id = productService.createProduct(memberAttributes, createProductRequest)
        return ResponseEntity.ok(id)
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Product 수정", description = "Product 수정 컨트롤러")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Product 수정성공",
            content = [Content(schema = Schema(implementation = ProductResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Product 수정실패",
            content = [Content(schema = Schema(implementation = IllegalStateException::class))]
        )
    )
    @PutMapping("/{id}")
    fun productUpdate(
        @Parameter(hidden = true) @MemberAuthentication memberAttributes: MemberAttributes,
        @Parameter(description = "수정할 Product Id") @PathVariable id: Long,
        @RequestBody updateProductRequest: UpdateProductRequest
    ): ResponseEntity<Unit> {
        productService.updateProduct(memberAttributes, id, updateProductRequest)
        return ResponseEntity.ok().build()
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Product 삭제", description = "Product 삭제 컨트롤러")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Product 삭제성공",
            content = [Content(schema = Schema(implementation = ProductResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Product 삭제실패",
            content = [Content(schema = Schema(implementation = IllegalStateException::class))]
        )
    )
    @DeleteMapping("/{id}")
    fun productDelete(
        @Parameter(hidden = true) @MemberAuthentication memberAttributes: MemberAttributes,
        @Parameter(description = "삭제할 Product Id") @PathVariable id: Long
    ): ResponseEntity<Unit> {
        productService.deleteProduct(memberAttributes, id)
        return ResponseEntity.ok().build()
    }
}