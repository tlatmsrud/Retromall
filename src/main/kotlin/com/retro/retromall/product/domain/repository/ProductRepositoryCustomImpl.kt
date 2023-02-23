package com.retro.retromall.product.domain.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.member.domain.QMember.member
import com.retro.retromall.product.domain.QProduct.product
import com.retro.retromall.product.dto.ProductListResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : ProductRepositoryCustom {
    override fun selectProductList(pageable: Pageable): ProductListResponse {
        val data = jpaQueryFactory.select(
            Projections.constructor(
                ProductListResponse.Data::class.java,
                product.id,
                member.nickname,
                product.content,
                product.amount,
                product.thumbnail,
                product.createdAt,
                product.modifiedAt
            )
        )
            .from(product)
            .innerJoin(member).on(product.author.id.eq(member.id))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        return ProductListResponse(data)
    }
}