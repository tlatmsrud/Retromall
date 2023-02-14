package com.retro.retromall.product.domain

import javax.persistence.*

@Entity
@Table(name = "tb_product_image")
class ProductImage(
    @Column(name = "image_url", nullable = false, updatable = false)
    val imageUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    var product: Product
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}