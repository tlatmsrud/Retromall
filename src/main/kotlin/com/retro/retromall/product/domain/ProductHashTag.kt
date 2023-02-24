package com.retro.retromall.product.domain

import javax.persistence.*

@Entity
@Table(name = "tb_product_hashtag")
class ProductHashTag(
    product: Product,
    hashTag: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product = product

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "hashtag_name")
    @Column(name = "hashtag_name")
    val hashTag: String = hashTag
}