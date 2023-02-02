package com.retro.retromall.product

import com.retro.retromall.category.Category
import com.retro.retromall.hashtag.HashTag
import java.time.LocalDateTime
import java.util.stream.Collectors
import javax.persistence.*

@Entity
@Table(name = "tb_product")
class Product(
    content: String?,
    amount: Int,
    category: Category
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "content", nullable = true)
    var content: String? = content
        private set

    @Column(name = "amount", nullable = false)
    var amount: Int = amount
        private set

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val imageUrlList: MutableList<ProductImage> = mutableListOf()

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val productHashTagList: MutableList<ProductHashTag> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    var category: Category = setCategory(category)
        private set

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "modified_at")
    var modifiedAt: LocalDateTime? = null

    private fun setCategory(category: Category): Category {
        category.addProduct(this)
        return category
    }

    fun modifyContent(content: String?, amount: Int, category: Category) {
        this.content = content
        this.amount = amount
        this.category = category
        this.modifiedAt = LocalDateTime.now()
    }

    fun addImage(url: String) {
        val productImage = ProductImage(id = ProductImageId(productId = this.id!!, url = url), product = this)
        this.imageUrlList.add(productImage)
    }

    fun addImages(urlList: List<String>) {
        val productImageList = urlList.stream()
            .map { url -> ProductImage(id = ProductImageId(productId = this.id!!, url = url), product = this) }
            .collect(Collectors.toList())
        this.imageUrlList.addAll(productImageList)
    }

    fun addHashTag(hashTag: HashTag) {
        val productHashTag = ProductHashTag(id = ProductHashTagId(productId = this.id!!, tag = hashTag.tag), product = this, tag = hashTag)
        this.productHashTagList.add(productHashTag)
    }
}