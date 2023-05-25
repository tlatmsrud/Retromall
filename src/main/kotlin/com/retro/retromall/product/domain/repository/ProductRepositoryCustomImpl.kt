package com.retro.retromall.product.domain.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.address.domain.QAddressEntity.addressEntity
import com.retro.retromall.member.domain.QMemberEntity.memberEntity
import com.retro.retromall.product.domain.ProductEntity
import com.retro.retromall.product.domain.ProductLikeEntity
import com.retro.retromall.product.domain.QProductEntity.productEntity
import com.retro.retromall.product.domain.QProductHashTagEntity.productHashTagEntity
import com.retro.retromall.product.domain.QProductImageEntity.productImageEntity
import com.retro.retromall.product.domain.QProductLikeEntity.productLikeEntity
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
        return getProductByMultipleRequest(memberId, productId)
    }

    private fun getProductByMultipleRequest(memberId: Long?, productId: Long): ProductResponse {
        val tuple = memberId?.let {
            jpaQueryFactory.select(productEntity, memberEntity.nickname, productLikeEntity.isLiked, addressEntity.addr)
                .from(productEntity)
                .innerJoin(memberEntity).on(productEntity.authorId.eq(memberEntity.id))
                .innerJoin(addressEntity).on(productEntity.addressId.eq(addressEntity.id))
                .leftJoin(productLikeEntity).on(productEntity.id.eq(productLikeEntity.productEntity.id).and(productLikeEntity.memberId.eq(it)))
                .where(productEntity.id.eq(productId))
                .fetchOne()
        } ?: jpaQueryFactory.select(productEntity, memberEntity.nickname, addressEntity.addr)
            .from(productEntity)
            .innerJoin(memberEntity).on(productEntity.authorId.eq(memberEntity.id))
            .innerJoin(addressEntity).on(productEntity.addressId.eq(addressEntity.id))
            .where(productEntity.id.eq(productId))
            .fetchOne()

        return tuple?.let {
            createProductResponse(
                memberId, tuple.get(productEntity)!!, tuple.get(memberEntity.nickname)!!, tuple.get(productLikeEntity.isLiked),
                getHashTags(tuple.get(productEntity)!!), getImages(tuple.get(productEntity)!!), tuple.get(addressEntity.addr)!!
            )
        } ?: throw IllegalStateException("요청하신 결과가 없습니다.")

    }

    private fun getProductByOncePerRequest(memberId: Long?, productId: Long): ProductResponse {
        val tuples = memberId?.let {
            jpaQueryFactory.select(
                productEntity, memberEntity.nickname, productLikeEntity.isLiked, addressEntity.addr, productHashTagEntity.id.hashTagName, productImageEntity.id.url)
                .from(productEntity)
                .innerJoin(memberEntity).on(productEntity.authorId.eq(memberEntity.id))
                .innerJoin(addressEntity).on(productEntity.addressId.eq(addressEntity.id))
                .innerJoin(productHashTagEntity).on(productEntity.eq(productHashTagEntity.productEntity))
                .innerJoin(productImageEntity).on(productEntity.eq(productImageEntity.productEntity))
                .leftJoin(productLikeEntity).on(productEntity.id.eq(productLikeEntity.productEntity.id).and(productLikeEntity.memberId.eq(it)))
                .where(productEntity.id.eq(productId))
                .fetch()
        } ?: jpaQueryFactory.select(productEntity, memberEntity.nickname, addressEntity.addr, productHashTagEntity.id.hashTagName, productImageEntity.id.url)
            .from(productEntity)
            .innerJoin(memberEntity).on(productEntity.authorId.eq(memberEntity.id))
            .innerJoin(productHashTagEntity).on(productEntity.eq(productHashTagEntity.productEntity))
            .innerJoin(productImageEntity).on(productEntity.eq(productImageEntity.productEntity))
            .innerJoin(addressEntity).on(productEntity.addressId.eq(addressEntity.id))
            .where(productEntity.id.eq(productId))
            .fetch()

        if (tuples.isNotEmpty()) {
            val product = tuples[0].get(productEntity)!!
            val author = tuples[0].get(memberEntity.nickname)!!
            val isLiked = tuples[0].get(productLikeEntity.isLiked)
            val address = tuples[0].get(addressEntity.addr)!!
            val hashTags = tuples.mapNotNull { it.get(productHashTagEntity.id.hashTagName) }.toSet()
            val images = tuples.mapNotNull { it.get(productImageEntity.id.url) }.toSet()
            return createProductResponse(memberId, product, author, isLiked, hashTags, images, address)
        }

        throw IllegalStateException("요청하신 결과가 없습니다.")
    }

    private fun createProductResponse(
        memberId: Long?,
        productEntity: ProductEntity,
        author: String,
        isLiked: Boolean?,
        hashTags: Set<String>,
        images: Set<String>,
        address: String
    ): ProductResponse {
        return ProductResponse(
            isAuthor = memberId?.let { productEntity.isAuthor(it) } ?: false,
            productId = productEntity.id!!,
            title = productEntity.title,
            content = productEntity.content,
            amount = productEntity.amount,
            author = author,
            category = productEntity.category,
            likes = productEntity.likes,
            isLiked = isLiked ?: false,
            hashTags = hashTags,
            images = images,
            address = address,
            createdAt = productEntity.createdAt,
            modifiedAt = productEntity.modifiedAt
        )
    }

    override fun selectProductList(category: String?, pageable: Pageable): ProductListResponse {
        val query = jpaQueryFactory.select(
            Projections.constructor(
                ProductListResponse.Data::class.java,
                productEntity.id,
                memberEntity.nickname,
                productEntity.title,
                productEntity.amount,
                productEntity.likes,
                productEntity.thumbnail,
                addressEntity.addr,
                productEntity.createdAt,
                productEntity.modifiedAt
            )
        )
            .from(productEntity)
            .innerJoin(memberEntity).on(productEntity.authorId.eq(memberEntity.id))
            .innerJoin(addressEntity).on(productEntity.addressId.eq(addressEntity.id))
            .where(eqCategory(category))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong() + 1)
        QueryDslUtils.setOrderBy(query, productEntity.type, productEntity.metadata, pageable)

        val content = query.fetch()

        var hasNext = false
        if (content.size > pageable.pageSize) {
            content.removeAt(pageable.pageSize)
            hasNext = true
        }

        return ProductListResponse(data = SliceImpl(content, pageable, hasNext))
    }

    override fun selectProductLike(productId: Long, memberId: Long): ProductLikeEntity? {
        return jpaQueryFactory.selectFrom(productLikeEntity)
            .where(productLikeEntity.productEntity.id.eq(productId).and(productLikeEntity.memberId.eq(memberId)))
            .fetchOne()
    }

    private fun eqCategory(category: String?): BooleanExpression? {
        return if (StringUtils.hasText(category))
            return productEntity.category.eq(category)
        else null
    }

    private fun getHashTags(productEntity: ProductEntity): Set<String> {
        return productEntity.hashTags.stream().map { it.id.hashTagName }.collect(Collectors.toSet())
    }

    private fun getImages(productEntity: ProductEntity): Set<String> {
        return productEntity.images.stream().map { it.id.url }.collect(Collectors.toSet())
    }
}