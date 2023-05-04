package com.retro.retromall.product.domain.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.address.domain.QAddress.address
import com.retro.retromall.member.domain.QMember.member
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductLike
import com.retro.retromall.product.domain.QProduct
import com.retro.retromall.product.domain.QProduct.product
import com.retro.retromall.product.domain.QProductLike.productLike
import com.retro.retromall.product.dto.ProductListResponse
import com.retro.retromall.product.dto.ProductResponse
import com.retro.util.QueryDslUtils
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import java.util.stream.Collectors

@Repository
class ProductRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : ProductRepositoryCustom {
    override fun selectProduct(productId: Long, memberId: Long?): ProductResponse {
//        val likeExpression = CaseBuilder()
//            .`when`(productLike.memberId.eq(memberId).and(productLike.isLiked.isTrue)).then(true)
//            .otherwise(false)
        val query = memberId?.let {
            jpaQueryFactory.select(product, member.nickname, productLike.isLiked, product.address.addr)
                .from(product)
                .innerJoin(member).on(product.authorId.eq(member.id))
                .leftJoin(productLike)
                .on(product.id.eq(productLike.product.id).and(productLike.memberId.eq(memberId)))
                .where(product.id.eq(productId))
                .fetchOne()
        } ?: jpaQueryFactory.select(product, member.nickname, product.address.addr)
            .from(product)
            .innerJoin(member).on(product.authorId.eq(member.id))
            .where(product.id.eq(productId))
            .fetchOne()

        query?.let {
            val product = query.get(product)!!
            val isLiked = query.get(productLike.isLiked)
            return ProductResponse(
                isAuthor = memberId?.let { product.isAuthor(it) } ?: false,
                productId = product.id!!,
                title = product.title,
                content = product.content,
                amount = product.amount,
                author = query.get(member.nickname)!!,
                category = product.category,
                likes = product.likes,
                isLiked = isLiked ?: false,
                hashTags = getHashTags(product),
                images = getImages(product),
                addr = product.address.addr,
                createdAt = product.createdAt,
                modifiedAt = product.modifiedAt
            )
        } ?: throw IllegalArgumentException("요청하신 결과가 없습니다.")
    }

    override fun selectProductList(category: String?, pageable: Pageable): ProductListResponse {
        val query = jpaQueryFactory.select(
            Projections.constructor(
                ProductListResponse.Data::class.java,
                product.id,
                member.nickname,
                product.title,
                product.amount,
                product.likes,
                product.thumbnail,
                product.address.addr,
                product.createdAt,
                product.modifiedAt
            )
        )
            .from(product)
            .innerJoin(member).on(product.authorId.eq(member.id))
            .where(eqCategory(category))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong() + 1)
        QueryDslUtils.setOrderBy(query, product.type, product.metadata, pageable)

        val content = query.fetch()

        var hasNext = false
        if (content.size > pageable.pageSize) {
            content.removeAt(pageable.pageSize)
            hasNext = true
        }

        return ProductListResponse(data = SliceImpl(content, pageable, hasNext))
    }

    override fun selectProductLike(productId: Long, memberId: Long): ProductLike? {
        return jpaQueryFactory.selectFrom(productLike)
            .where(productLike.product.id.eq(productId).and(productLike.memberId.eq(memberId)))
            .fetchOne()
    }

    private fun eqCategory(category: String?): BooleanExpression? {
        return if (StringUtils.hasText(category))
            return product.category.eq(category)
        else null
    }

    private fun getHashTags(product: Product): Set<String> {
        return product.hashTags.stream().map { it.hashTag }.collect(Collectors.toSet())
    }

    private fun getImages(product: Product): Set<String> {
        return product.images.stream().map { it.imageUrl }.collect(Collectors.toSet())
    }
}