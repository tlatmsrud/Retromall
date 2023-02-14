package com.retro.retromall.category.controller

import com.retro.retromall.category.dto.CategoryListResponse
import com.retro.retromall.category.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    @GetMapping
    fun categoryList(@RequestParam("root") root: String): ResponseEntity<CategoryListResponse> {
        val body = categoryService.findCategoryList(root)
        return ResponseEntity.ok(body)
    }
}