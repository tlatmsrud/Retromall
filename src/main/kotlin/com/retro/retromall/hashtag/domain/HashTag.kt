package com.retro.retromall.hashtag.domain

import com.retro.retromall.product.domain.ProductHashTag
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_hashtag")
class HashTag(
    tag: String
) {
    @Id
    @Column(name = "tag", updatable = false, nullable = false)
    val tag: String = tag

    @Column(name = "created_at", updatable = false, nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @OneToMany(mappedBy = "hashTag", fetch = FetchType.LAZY)
    val productHashTag: MutableList<ProductHashTag> = mutableListOf()
}