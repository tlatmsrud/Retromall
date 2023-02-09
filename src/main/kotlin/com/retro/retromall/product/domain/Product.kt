package com.retro.retromall.product.domain

import com.retro.retromall.category.domain.Category
import com.retro.retromall.hashtag.domain.HashTag
import java.time.LocalDateTime
import java.util.stream.Collectors
import javax.persistence.*

@Entity
@Table(name = "tb_product")
class Product(
    content: String,
    amount: Int,
    category: Category,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "content", nullable = true)
    var content: String = content
        private set

    @Column(name = "amount", nullable = false)
    var amount: Int = amount
        private set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name", referencedColumnName = "category_name")
    var category: Category = category
        private set

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var imageUrls: MutableList<ProductImage> = mutableListOf()
        private set

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var hashTags: MutableList<ProductHashTag> = mutableListOf()
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

    fun addImages(urlList: MutableList<String>) {
        val productImageList = urlList.stream()
            .map { url -> ProductImage(id = ProductImageId(productId = this.id!!, url = url), product = this) }
            .collect(Collectors.toList())
        this.imageUrls.addAll(productImageList)
    }

    fun addHashTags(hashTags: MutableList<String>) {
        val productHashTagList = hashTags.stream().map { tag ->
            ProductHashTag(hashTag = HashTag(tag), product = this)
        }.collect(Collectors.toList())
        this.hashTags.addAll(productHashTagList)
    }
}