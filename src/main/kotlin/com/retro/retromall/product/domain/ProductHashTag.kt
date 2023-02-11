package com.retro.retromall.product.domain

import com.retro.retromall.hashtag.domain.HashTag
import javax.persistence.*

@Entity
@Table(name = "tb_product_hashtag")
class ProductHashTag(
    product: Product,
    hashTag: HashTag
) {
    @EmbeddedId
    val id: ProductHashTagId = ProductHashTagId(productId = product.id!!, tag = hashTag.tag)

    @MapsId(value = "productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    var product: Product = product
        private set

    @MapsId(value = "tag")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", referencedColumnName = "hashtag_id")
    var hashTag: HashTag = hashTag
        private set
}