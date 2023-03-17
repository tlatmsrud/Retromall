package com.retro.retromall.product.controller

import com.retro.common.annotation.MemberAuthentication
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.dto.ProductListResponse
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.service.ProductReadService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductReadController(
    private val productReadService: ProductReadService
) {

    @GetMapping("/{id}")
    fun product(
        @MemberAuthentication(required = false) memberAttributes: MemberAttributes,
        @PathVariable id: Long
    ): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(productReadService.getProduct(memberAttributes, id))
    }

    @GetMapping
    fun productList(
        @RequestParam("category") category: String?,
        @PageableDefault(
            size =
            20,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<ProductListResponse> {
        return ResponseEntity.ok(productReadService.getProductList(category, pageable))
    }
}