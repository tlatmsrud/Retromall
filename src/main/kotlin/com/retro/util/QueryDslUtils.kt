package com.retro.util

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.PathMetadata
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Pageable

class QueryDslUtils {
    companion object {
        fun <T> setOrderBy(query: JPAQuery<*>, classType: Class<T>, classMetadata: PathMetadata, pageable: Pageable) {
            for (sort in pageable.sort) {
                val pathBuilder = PathBuilder(classType, classMetadata)
                query.orderBy(
                    OrderSpecifier(
                        if (sort.isAscending) Order.ASC else Order.DESC,
                        pathBuilder.get(sort.property) as Expression<out Comparable<*>>
                    )
                )
            }
        }
    }
}