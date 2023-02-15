package com.retro.retromall.product.domain

import com.retro.retromall.category.domain.Category
import com.retro.retromall.hashtag.domain.HashTag
import com.retro.retromall.member.domain.Member
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_product")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    val id: Long? = null,

    @Column(name = "content", nullable = true)
    var content: String?,

    @Column(name = "amount", nullable = false)
    var amount: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "member_id")
    var author: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name", referencedColumnName = "category_name")
    var category: Category? = null,

    @ManyToMany(cascade = [CascadeType.MERGE])
    @JoinTable(
        name = "tb_product_hashtag",
        joinColumns = [JoinColumn(name = "product_id", referencedColumnName = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "hashtag_name", referencedColumnName = "hashtag_name")]
    )
    var hashtags: MutableSet<HashTag> = mutableSetOf(),

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var images: MutableSet<ProductImage> = mutableSetOf(),

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "modified_at")
    var modifiedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(
        content: String?,
        amount: Int,
    ) : this(null, content, amount)

    fun addHashTags(hashtags: List<HashTag>) {
        this.hashtags.addAll(hashtags)
        hashtags.forEach { it.products.add(this) }
    }

    fun addImages(images: List<ProductImage>) {
        this.images.addAll(images)
        images.forEach { it.product = this }
    }

    fun isAuthor(member: Member) {
        if (author != member)
            throw IllegalStateException("해당 상품을 수정할 권한이 없습니다.")
    }

    fun modifyProduct(content: String, amount: Int, category: Category) {
        this.content = content
        this.amount = amount
        this.category = category
        this.modifiedAt = LocalDateTime.now()
    }
}