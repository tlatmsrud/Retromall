package com.retro.retromall.product.domain

import javax.persistence.*

@Entity
@Table(name = "tb_product_hashtag")
class ProductHashTag(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    var id: Long? = null,
    @EmbeddedId
    val id: ProductHashTagId,

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,

//    @Column(name = "hashtag_name")
//    val hashTag: String
) {
    constructor(product: Product, hashTag: String) : this(ProductHashTagId(product.id!!, hashTag), product)
}