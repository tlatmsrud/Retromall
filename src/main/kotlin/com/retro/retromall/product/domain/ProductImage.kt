package com.retro.retromall.product.domain

import javax.persistence.*

@Entity
@Table(name = "tb_product_image")
class ProductImage(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product,

    @Column(name = "image_url", nullable = false, updatable = false)
    val imageUrl: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductImage

        if (product != other.product) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = product.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }
}