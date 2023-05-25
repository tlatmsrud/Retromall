package com.retro.retromall.category.domain.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.category.domain.CategoryEntity
import com.retro.retromall.category.domain.QCategoryEntity.categoryEntity
import com.retro.retromall.category.dto.CategoryListResponse
import com.retro.retromall.category.dto.CategoryResponse
import org.springframework.util.StringUtils
import java.util.stream.Collectors

class CustomCategoryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : CustomCategoryRepository {
    override fun selectCategoryListByRootCategory(root: String?): CategoryResponse {
        val result = jpaQueryFactory.select(categoryEntity.name)
            .from(categoryEntity)
            .where(eqRootCategory(root))
            .fetch()

        return CategoryResponse(result)
    }

    override fun selectCategories(): CategoryListResponse {
        val categories = jpaQueryFactory.selectFrom(categoryEntity).fetch()
        val rootCategories = categories.stream().filter { it.parent == null }.map { it.name }
        val data = rootCategories.map { createCategoryData(categories, it) }.collect(Collectors.toList())

        return CategoryListResponse(data)
    }

    private fun createCategoryData(categories: MutableList<CategoryEntity>, categoryName: String): CategoryListResponse.Data {
        val category = categories.find { it.name == categoryName }!!
        val lowerCategories = categories.filter { it.parent?.name == categoryName }.map { createCategoryData(categories, it.name) }
        return CategoryListResponse.Data(category.id, category.name, lowerCategories)
    }

    private fun eqRootCategory(root: String?): BooleanExpression? {
        return if (StringUtils.hasText(root))
            categoryEntity.parent.name.eq(root)
        else
            categoryEntity.parent.isNull
    }
}