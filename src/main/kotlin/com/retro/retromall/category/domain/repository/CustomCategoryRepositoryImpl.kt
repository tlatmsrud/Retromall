package com.retro.retromall.category.domain.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.category.domain.QCategory
import com.retro.retromall.category.dto.CategoryResponse
import org.springframework.util.StringUtils

class CustomCategoryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomCategoryRepository {
    override fun selectCategoryListByRootCategory(root: String?): CategoryResponse {
        val result = jpaQueryFactory.select(QCategory.category.name)
            .from(QCategory.category)
            .where(eqRootCategory(root))
            .fetch()

        return CategoryResponse(result)
    }

    private fun eqRootCategory(root: String?): BooleanBuilder {
        val booleanBuilder = BooleanBuilder()
        if (!StringUtils.hasText(root))
            booleanBuilder.and(QCategory.category.parent.isNull)
        else
            booleanBuilder.and(QCategory.category.parent.name.eq(root))

        return booleanBuilder
    }
}