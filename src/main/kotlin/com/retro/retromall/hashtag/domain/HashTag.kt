package com.retro.retromall.hashtag.domain

import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductHashTag
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_hashtag")
class HashTag(
    tag: String
) {
    @Id
    @Column(name = "hashtag_id", updatable = false, nullable = false)
    var id: Long? = null

    @Column(name = "tag_name", nullable = false)
    val name: String = tag

    @Column(name = "created_at", updatable = false, nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @ManyToMany(mappedBy = "hashtags")
    var products: MutableSet<Product> = mutableSetOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HashTag) return false

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}