package com.retro.retromall.category.domain.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.category.domain.Category
import com.retro.retromall.category.domain.QCategory.category
import com.retro.retromall.category.dto.CategoryListResponse
import com.retro.retromall.category.dto.CategoryResponse
import org.springframework.util.StringUtils
import java.util.stream.Collectors
import javax.persistence.EntityManager

class CustomCategoryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val entityManager: EntityManager
) : CustomCategoryRepository {
    override fun selectCategoryListByRootCategory(root: String?): CategoryResponse {
        val result = jpaQueryFactory.select(category.name)
            .from(category)
            .where(eqRootCategory(root))
            .fetch()

        return CategoryResponse(result)
    }

    override fun selectCategories(): CategoryListResponse {
        val categories = jpaQueryFactory.selectFrom(category).fetch()
        val rootCategories = categories.stream().filter { it.parent == null }.map { it.name }
        val data = rootCategories.map { createCategoryData(categories, it) }.collect(Collectors.toList())

        return CategoryListResponse(data)
    }

    private fun createCategoryData(categories: MutableList<Category>, categoryName: String): CategoryListResponse.Data {
        val category = categories.find { it.name == categoryName }!!
        val lowerCategories = categories.filter { it.parent?.name == categoryName }.map { createCategoryData(categories, it.name) }
        return CategoryListResponse.Data(category.name, lowerCategories)
    }

    private fun eqRootCategory(root: String?): BooleanExpression? {
        return if (StringUtils.hasText(root))
            category.parent.name.eq(root)
        else
            category.parent.isNull
    }
}