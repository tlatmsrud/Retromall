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
    val isLiked: Boolean = true

    fun isLiked(memberId: Long): Boolean {
        if (this.memberId == memberId && isLiked)
            return true
        return false
    }
}