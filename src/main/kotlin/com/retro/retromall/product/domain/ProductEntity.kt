package com.retro.retromall.product.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_product")
class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    val id: Long? = null,

    @Column(name = "title", length = 255, nullable = false)
    var title: String,

    @Column(name = "content", length = 5000, nullable = true)
    var content: String?,

    @Column(name = "amount", nullable = false)
    var amount: Int,

    @Column(name = "author_id")
    var authorId: Long,

    @Column(name = "category", length = 50)
    var category: String,

    @JoinColumn(name = "address_id")
    var addressId: Long,

    @Column(name = "thumbnail", length = 255)
    var thumbnail: String? = null,

    @Column(name = "likes")
    var likes: Long = 0L,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "modified_at", nullable = false)
    var modifiedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "productEntity", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var productLikeEntities: MutableList<ProductLikeEntity> = mutableListOf(),

    @OneToMany(mappedBy = "productEntity", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var hashTags: MutableSet<ProductHashTagEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "productEntity", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var images: MutableSet<ProductImageEntity> = mutableSetOf(),

    ) {
    constructor(
        title: String,
        content: String?,
        amount: Int,
        authorId: Long,
        category: String,
        addressId: Long
    ) : this(null, title, content, amount, authorId, category, addressId)

    fun addLikes(memberId: Long) {
        val productLike = this.productLikeEntities.find { it.memberId == memberId }
        productLike?.let {
            if (!it.isLiked) {
                it.isLiked = true
                this.likes++
            }
        } ?: run {
            this.productLikeEntities.add(ProductLikeEntity(this, memberId))
            this.likes++
        }
    }

    fun removeLikes(memberId: Long) {
        val productLike = this.productLikeEntities.find { it.memberId == memberId }
        productLike?.let {
            if (it.isLiked) {
                it.isLiked = false
                this.likes--
            }
        } ?: throw IllegalStateException("해당 상품에 대해 좋아요를 누른적이 없습니다.")
    }

    fun isAuthor(memberId: Long): Boolean {
        return authorId == memberId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}