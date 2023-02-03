package com.retro.retromall.product.domain

import java.io.Serializable
import javax.persistence.*

@Embeddable
class ProductImageId(
    productId: Long,
    url: String,
): Serializable {
    companion object {
        const val serialVersionUID = -2414832548258431L
    }

    @Column(name = "product_id", updatable = false, nullable = false)
    val productId: Long = productId

    @Column(name = "url", updatable = true, nullable = false)
    var url: String = url
        private set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductImageId

        if (productId != other.productId) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = productId.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }
}