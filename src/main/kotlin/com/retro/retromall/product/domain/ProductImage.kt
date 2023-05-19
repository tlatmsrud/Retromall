package com.retro.retromall.product.domain

import javax.persistence.*

@Entity
@Table(name = "tb_product_image")
class ProductImage(
    @EmbeddedId
    val id: ProductImageId,

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product,
) {
    constructor(product: Product, image: String) : this(ProductImageId(product.id!!, image), product)
}