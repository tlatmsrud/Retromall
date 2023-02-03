package com.retro.retromall.repository

import com.retro.retromall.category.domain.Category
import com.retro.retromall.category.domain.CategoryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class CategoryRepositoryTest(
    @Autowired
    private val categoryRepository: CategoryRepository,
) {
    @BeforeEach
    fun init() {
        var category = Category(category = "PC", korValue = "데스크탑")
        categoryRepository.save(category)
    }

    private fun addChild(parent: Category) {
        val child1 = Category(category = "CPU", korValue = "CPU")
        child1.addParent(parent)

        val child2 = Category(category = "Graphic", korValue = "Graphic")
        child2.addParent(parent)

        val child3 = Category(category = "Power", korValue = "Power")
        child3.addParent(parent)

        categoryRepository.save(child1)
        categoryRepository.save(child2)
        categoryRepository.save(child3)
    }

    @Test
    fun equalsCategory() {
        var category = categoryRepository.findById("PC").orElseThrow()

        assertEquals(category.korValue, "데스크탑")
        assertEquals(category.products.size, 4)
    }

    @Test
    fun addChild() {
        //given
        var parent = categoryRepository.findById("PC").orElseThrow()
        addChild(parent)

        //when
        var child1 = categoryRepository.findById("CPU").orElseThrow()
        var child2 = categoryRepository.findById("Graphic").orElseThrow()
        var child3 = categoryRepository.findById("Power").orElseThrow()

        //then
        assertEquals(child1.category, "CPU")
        assertEquals(child1.parent, parent)

        assertEquals(child2.category, "Graphic")
        assertEquals(child2.parent, parent)

        assertEquals(child3.category, "Power")
        assertEquals(child3.parent, parent)

        assertEquals(parent.childList.size, 3)
    }
}