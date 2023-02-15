package com.retro.retromall.category.service

import com.retro.retromall.category.domain.Category
import com.retro.retromall.category.domain.repository.CategoryRepository
import com.retro.retromall.category.dto.CategoryListResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CategoryReadService(
    private val categoryRepository: CategoryRepository
) {
    fun getCategory(name: String): Category {
        return categoryRepository.findById(name)
            .orElseThrow { IllegalArgumentException("존재하지 않는 카테고리입니다.") }
    }
    fun getCategoryList(root: String): CategoryListResponse {
        return categoryRepository.selectCategoryListByRootCategory(root);
    }
}