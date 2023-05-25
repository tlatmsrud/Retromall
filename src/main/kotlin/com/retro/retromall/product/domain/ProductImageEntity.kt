package com.retro.retromall.product.domain

import javax.persistence.*

@Entity
@Table(name = "tb_product_image")
class ProductImageEntity(
    @EmbeddedId
    val id: ProductImageEntityId,

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var productEntity: ProductEntity,
) {
    constructor(productEntity: ProductEntity, image: String) : this(ProductImageEntityId(productEntity.id!!, image), productEntity)
}