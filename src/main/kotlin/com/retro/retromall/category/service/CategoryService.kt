package com.retro.retromall.category.service

import com.retro.retromall.category.domain.repository.CategoryRepository
import com.retro.retromall.category.dto.CategoryListResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    fun findCategoryList(root: String): CategoryListResponse {
        return categoryRepository.selectCategoryListByRootCategory(root);
    }
}