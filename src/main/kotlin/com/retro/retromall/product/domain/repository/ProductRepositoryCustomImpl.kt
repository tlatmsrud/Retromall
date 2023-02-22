package com.retro.retromall.product.domain.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.category.domain.QCategory.category
import com.retro.retromall.member.domain.QMember.member
import com.retro.retromall.product.domain.QProduct.product
import com.retro.retromall.product.domain.QProductHashTag.productHashTag
import com.retro.retromall.product.domain.QProductImage.productImage
import com.retro.retromall.product.dto.ProductResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import kotlin.streams.toList


@Repository
class ProductRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : ProductRepositoryCustom {
    private val logger: Logger = LoggerFactory.getLogger(ProductRepositoryCustomImpl::class.java)

    override fun selectAllProductResponseByCategoryName(categoryName: String?): List<ProductResponse> {
        val products = jpaQueryFactory.select(
            product,
            member.nickname,
            category.name
        )
            .from(product)
            .innerJoin(product.author, member).fetchJoin()
            .innerJoin(product.category, category).fetchJoin()
            .fetch()

        return products.stream().map {
            logger.info("{}", it)
            ProductResponse(
                it.get(product.id)!!,
                it.get(product.content),
                it.get(product.amount)!!,
                it.get(product.author.nickname),
                it.get(product.category.name)!!,
                getHashTagsStringByProductId(it.get(product.id)!!),
                getProductImagesUrlByProductId(it.get(product.id)!!),
                it.get(product.createdAt)!!,
                it.get(product.modifiedAt)!!
            )
        }.toList()
    }

    /**
     * N + 1 문제발생 캐시를 써서 해결?
     */
    private fun getHashTagsStringByProductId(productId: Long): Set<String> {
        return jpaQueryFactory
            .select(productHashTag.hashTag.name)
            .from(productHashTag)
            .where(productHashTag.product.id.eq(productId))
            .fetch().toSet()
    }

    private fun getProductImagesUrlByProductId(productId: Long): Set<String> {
        return jpaQueryFactory
            .select(productImage.imageUrl)
            .from(productImage)
            .where(productImage.product.id.eq(productId))
            .fetch().toSet()
    }

    private fun eqCategory(categoryName: String?): BooleanExpression? {
        return categoryName?.let { product.category.name.eq(categoryName) }
    }
}