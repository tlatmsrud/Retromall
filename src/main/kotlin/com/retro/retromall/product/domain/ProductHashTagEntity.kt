package com.retro.retromall.product.domain

import javax.persistence.*

@Entity
@Table(name = "tb_product_hashtag")
class ProductHashTagEntity(
    @EmbeddedId
    val id: ProductHashTagEntityId,

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val productEntity: ProductEntity,
) {
    constructor(productEntity: ProductEntity, hashTag: String) : this(ProductHashTagEntityId(productEntity.id!!, hashTag), productEntity)
}