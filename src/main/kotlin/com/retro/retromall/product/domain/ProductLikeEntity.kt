package com.retro.retromall.product.domain

import javax.persistence.*

@Entity
@Table(name = "tb_product_like",
    uniqueConstraints = [UniqueConstraint(columnNames = ["product_id", "member_id"])])
class ProductLikeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val productEntity: ProductEntity,

    @Column(name = "member_id")
    val memberId: Long,

    @Column(name = "is_liked")
    var isLiked: Boolean
) {
    constructor(
        productEntity: ProductEntity,
        memberId: Long
    ) : this(null, productEntity, memberId, true)

    fun isLiked(memberId: Long): Boolean {
        return this.memberId == memberId && isLiked
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductLikeEntity

        if (productEntity != other.productEntity) return false
        return memberId == other.memberId
    }

    override fun hashCode(): Int {
        var result = productEntity.hashCode()
        result = 31 * result + memberId.hashCode()
        return result
    }
}