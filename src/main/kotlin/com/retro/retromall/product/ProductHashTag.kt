package com.retro.retromall.product

import com.retro.retromall.hashtag.HashTag
import javax.persistence.*

@Entity
@Table(name = "tb_product_hashtag")
class ProductHashTag(
    id: ProductHashTagId,
    product: Product,
    tag: HashTag
) {
    @EmbeddedId
    val id: ProductHashTagId = id

    @MapsId(value = "productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    val product: Product = product

    @MapsId(value = "tag")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag", referencedColumnName = "tag")
    val tag: HashTag = tag
}