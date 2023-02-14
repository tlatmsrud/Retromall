package com.retro.retromall.product.domain

import com.retro.retromall.category.domain.Category
import com.retro.retromall.hashtag.domain.HashTag
import com.retro.retromall.member.domain.Member
import java.time.LocalDateTime
import java.util.stream.Collectors
import javax.persistence.*
import kotlin.streams.toList

@Entity
@Table(name = "tb_product")
class Product(
    author: Member,
    content: String?,
    amount: Int,
    category: Category?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "member_id")
    val author: Member = author

    @Column(name = "content", nullable = true)
    var content: String? = content
        private set

    @Column(name = "amount", nullable = false)
    var amount: Int = amount
        private set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name", referencedColumnName = "category_name")
    var category: Category? = category
        private set

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var imageUrls: MutableList<ProductImage> = mutableListOf()
        private set

    @ManyToMany
    @JoinTable(
        name = "ProductHashTag",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "hashtag_id")]
    )
    var hashtags: MutableSet<HashTag> = mutableSetOf()
        private set

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
        private set

    @Column(name = "modified_at")
    var modifiedAt: LocalDateTime = LocalDateTime.now()
        private set

    fun modifyProduct(content: String, amount: Int, category: Category) {
        this.content = content
        this.amount = amount
        this.category = category
        this.modifiedAt = LocalDateTime.now()
    }

    fun addImages(urlList: MutableList<String>?) {
        urlList?.let {
            val imageUrlLists = it.stream()
                .map { url -> ProductImage(id = ProductImageId(productId = this.id!!, url = url), product = this) }
                .collect(Collectors.toList())
            this.imageUrls.addAll(imageUrlLists)
        }
    }
}