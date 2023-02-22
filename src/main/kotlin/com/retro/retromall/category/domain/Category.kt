package com.retro.retromall.category.domain

import javax.persistence.*

@Entity
@Table(name = "tb_category")
class Category(
    isClassification: Boolean,
    parent: Category?,
    name: String,
) {
    @Id
    @Column(name = "category_name", nullable = false)
    val name: String = name

    @Column(name = "is_classification", nullable = false)
    val isClassification: Boolean = isClassification

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "parent", fetch = FetchType.LAZY)
    val lowerCategoryList: MutableList<Category> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    var parent: Category? = parent

    fun addLowerCategory(category: Category) {
        this.lowerCategoryList.add(category)
        category.parent = this
    }
}