package com.retro.retromall.category.service

import com.retro.retromall.category.domain.CategoryEntity
import com.retro.retromall.category.domain.repository.CategoryRepository
import com.retro.retromall.category.dto.CategoryListResponse
import com.retro.retromall.category.dto.CategoryResponse
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CategoryReadService(
    private val categoryRepository: CategoryRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) {
    fun getCategory(name: String): CategoryEntity {
        return categoryRepository.findById(name)
            .orElseThrow { IllegalArgumentException("존재하지 않는 카테고리입니다.") }
    }
    fun getCategoryList(root: String?): CategoryResponse {
        return categoryRepository.selectCategoryListByRootCategory(root)
    }

    @Cacheable("categories")
    fun getCategoryList(): CategoryListResponse {
        return categoryRepository.selectCategories()
    }
}