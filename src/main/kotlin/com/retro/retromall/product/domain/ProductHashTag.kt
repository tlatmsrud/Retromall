package com.retro.retromall.product.domain

import com.retro.retromall.hashtag.domain.HashTag
import javax.persistence.*

@Entity
@Table(name = "tb_product_hashtag")
class ProductHashTag(
    product: Product,
    hashTag: HashTag
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    val product: Product = product

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", referencedColumnName = "hashtag_id")
    val hashTag: HashTag = hashTag
}