package com.retro.retromall.service

import com.retro.retromall.category.service.CategoryService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles("dev")
class CategoryServiceTest(
    @Autowired
    private val categoryService: CategoryService
) {
    @Test
    fun findCategoryList() {
        //given
        val root = "Play Station"

        //when
        val result = categoryService.findCategoryList(root)

        //then
        assertEquals(4, result.categoryList.size)
    }

}