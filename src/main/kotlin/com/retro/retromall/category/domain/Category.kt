package com.retro.retromall.category.domain

import com.retro.retromall.product.domain.Product
import javax.persistence.*

@Entity
@Table(name = "tb_category")
class Category(
    category: String,
    korValue: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "category", nullable = false)
    val category: String = category

    @Column(name = "kor_val", nullable = false)
    var korValue: String = korValue
        private set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
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