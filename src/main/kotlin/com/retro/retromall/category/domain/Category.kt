package com.retro.retromall.category.domain

import com.retro.retromall.product.domain.Product
import javax.persistence.*

@Entity
@Table(name = "tb_category")
class Category(
    name: String,
) {
    @Id
    @Column(name = "category_name", nullable = false)
    val name: String = name

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_name", referencedColumnName = "category_name")
    var parent: Category? = null

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "parent", fetch = FetchType.LAZY)
    val childList: MutableList<Category> = mutableListOf()

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], mappedBy = "category", fetch = FetchType.LAZY)
    val products: MutableList<Product> = mutableListOf()

    fun addParent(parent: Category) {
        this.parent = parent
        this.parent!!.childList.add(this)
    }

    fun addProduct(product: Product) {
        this.products.add(product)
    }
}