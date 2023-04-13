package com.retro.retromall.hashtag.domain

import javax.persistence.*

@Entity
@Table(name = "tb_hashtag")
class HashTag(
    @Id
    @Column(name = "hashtag_name", nullable = false)
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HashTag) return false

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}