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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductHashTag

        if (product != other.product) return false
        if (hashTag != other.hashTag) return false

        return true
    }

    override fun hashCode(): Int {
        var result = product.hashCode()
        result = 31 * result + hashTag.hashCode()
        return result
    }


}