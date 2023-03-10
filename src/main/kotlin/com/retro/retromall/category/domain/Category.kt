package com.retro.retromall.category.domain

import javax.persistence.*

@Entity
@Table(name = "tb_category")
class Category(
    @Id
    @Column(name = "category_name", nullable = false)
    val name: String,

    @Column(name = "id", unique = true)
    val id: String,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "parent", fetch = FetchType.LAZY)
    val lowerCategoryList: MutableList<Category> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    var parent: Category?
) {
    fun addLowerCategory(category: Category) {
        this.lowerCategoryList.add(category)
        category.parent = this
    }
}