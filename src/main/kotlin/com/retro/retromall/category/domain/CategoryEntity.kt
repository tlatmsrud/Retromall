package com.retro.retromall.category.domain

import javax.persistence.*

@Entity
@Table(name = "tb_category")
class CategoryEntity(
    @Id
    @Column(name = "category_name", nullable = false)
    val name: String,

    @Column(name = "id", unique = true)
    val id: String,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "parent", fetch = FetchType.LAZY)
    val lowerCategoryListEntity: MutableList<CategoryEntity> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    var parent: CategoryEntity?
) {
    fun addLowerCategory(categoryEntity: CategoryEntity) {
        this.lowerCategoryListEntity.add(categoryEntity)
        categoryEntity.parent = this
    }
}