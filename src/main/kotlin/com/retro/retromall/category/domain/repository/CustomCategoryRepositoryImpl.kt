package com.retro.retromall.category.domain.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.category.domain.QCategory.category
import com.retro.retromall.category.dto.CategoryResponse

class CustomCategoryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomCategoryRepository {
    override fun selectCategoryListByRootCategory(root: String?): CategoryResponse {
        val result = jpaQueryFactory.select(category.name)
            .from(category)
            .where(eqRootCategory(root))
            .fetch()

        return CategoryResponse(result)
    }

    private fun eqRootCategory(root: String?): BooleanExpression? {
        return root?.let {
            category.parent.name.eq(root)
        } ?: let {
            category.parent.isNull
        }
    }
}