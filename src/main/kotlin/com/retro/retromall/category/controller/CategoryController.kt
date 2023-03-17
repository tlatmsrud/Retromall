package com.retro.retromall.category.controller

import com.retro.retromall.category.dto.CategoryListResponse
import com.retro.retromall.category.service.CategoryReadService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryReadService: CategoryReadService
) {
    @GetMapping
    fun categoryList(): ResponseEntity<CategoryListResponse> {
        return ResponseEntity.ok(categoryReadService.getCategoryList())
    }
}