package com.retro.retromall.product.domain.repository.projection

import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.member.domain.QMember.member
import com.retro.retromall.product.domain.ProductHashTag
import com.retro.retromall.product.domain.ProductImage
import com.retro.retromall.product.domain.QProduct.product
import com.retro.retromall.product.domain.QProductHashTag.productHashTag
import com.retro.retromall.product.domain.QProductImage.productImage
import com.retro.retromall.product.domain.repository.ProductRepositoryCustom
import com.retro.retromall.product.dto.ProductListResponse
import com.retro.retromall.product.dto.ProductResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class ProductRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : ProductRepositoryCustom {
    override fun selectProduct(productId: Long): ProductResponse {

        val data = jpaQueryFactory.select(
            product.id,
            product.content,
            product.amount,
            member.nickname,
            product.category,
            productHashTag.hashTag,
            productImage.imageUrl,
            product.createdAt,
            product.modifiedAt
        )
            .from(product)
            .innerJoin(member).on(product.authorId.eq(member.id))
            .innerJoin(productHashTag).on(product.id.eq(productHashTag.product.id))
            .innerJoin(productImage).on(product.id.eq(productImage.product.id))
            .where(product.id.eq(productId))
            .fetchOne()

        data?.let {
            return ProductResponse(
                productId = data.get(product.id)!!,
                content = data.get(product.content),
                data.get(product.amount)!!,
                data.get(member.nickname)!!,
                data.get(product.category)!!,
                getHashTags(data.get(product.hashTags)),
                getImages(data.get(product.images)),
                data.get(product.createdAt)!!,
                data.get(product.modifiedAt)!!
            )
        } ?: throw IllegalArgumentException("요청하신 결과가 없습니다.")
    }

    override fun selectProductList(pageable: Pageable): ProductListResponse {
        TODO("Not yet implemented")
    }

    private fun getHashTags(data: MutableSet<ProductHashTag>?): Set<String> {
        return data?.stream()?.map { it.hashTag }?.collect(Collectors.toSet()) ?: mutableSetOf()
    }

    private fun getImages(data: MutableSet<ProductImage>?): Set<String> {
        return data?.stream()?.map { it.imageUrl }?.collect(Collectors.toSet()) ?: mutableSetOf()
    }
}