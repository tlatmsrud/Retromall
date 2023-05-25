package com.retro.retromall.product.domain

import java.io.Serializable
import javax.persistence.*

@Embeddable
class ProductImageEntityId(
    @Column(name = "product_id", updatable = false, nullable = false)
    val productId: Long,

    @Column(name = "image_url", updatable = true, nullable = false)
    val url: String,
): Serializable {
    companion object {
        const val serialVersionUID = -2414832548258431L
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductImageEntityId

        if (productId != other.productId) return false
        return url == other.url
    }

    override fun hashCode(): Int {
        var result = productId.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }
}