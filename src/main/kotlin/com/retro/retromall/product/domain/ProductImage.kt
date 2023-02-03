package com.retro.retromall.product.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_product_image")
class ProductImage(
    id: ProductImageId,
    product: Product
) {
    @EmbeddedId
    val id: ProductImageId = id

    @MapsId(value = "productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    val product: Product = product

    @Column(name = "created_at", updatable = false, nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "modified_at", updatable = true, nullable = true)
    var modifiedAt: LocalDateTime? = null
        private set
}