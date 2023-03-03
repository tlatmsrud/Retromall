package com.retro.retromall.product.domain

import javax.persistence.*

@Entity
@Table(name = "tb_product_like", uniqueConstraints = [UniqueConstraint(columnNames = ["product_id", "member_id"])])
class ProductLike(
    product: Product,
    memberId: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product = product

    @Column(name = "member_id")
    val memberId: Long = memberId

    @Column(name = "is_liked")
    var isLiked: Boolean = true

    fun isLiked(memberId: Long): Boolean {
        if (this.memberId == memberId && isLiked)
            return true
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductLike

        if (product != other.product) return false
        if (memberId != other.memberId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = product.hashCode()
        result = 31 * result + memberId.hashCode()
        return result
    }
}