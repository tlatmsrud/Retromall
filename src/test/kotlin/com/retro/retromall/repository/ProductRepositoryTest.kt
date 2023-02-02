package com.retro.retromall.repository

import com.retro.retromall.category.Category
import com.retro.retromall.category.CategoryRepository
import com.retro.retromall.hashtag.HashTag
import com.retro.retromall.hashtag.HashTagRepository
import com.retro.retromall.product.Product
import com.retro.retromall.product.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
class ProductRepositoryTest(
    @Autowired
    private var productRepository: ProductRepository,

    @Autowired
    private var categoryRepository: CategoryRepository,

    @Autowired
    private var hashTagRepository: HashTagRepository
) {
    @BeforeEach
    fun init() {
        var category: Category = createCategory()
        createHashTag()
        val product =
            Product(content = "content", amount = 12000, category)

        val result = productRepository.save(product)
    }

    private fun createCategory(): Category {
        return categoryRepository.save(Category(category = "PC", korValue = "데스크탑"))
    }

    private fun createHashTag(): HashTag {
        return hashTagRepository.save(HashTag(tag = "PS5"))
    }

    @Test
    fun addImage() {
        //given
        val product = productRepository.getReferenceById(1L)

        //when
        product.addImage("imageUrl1")

        //then
        assertEquals(product.imageUrlList.size, 1)
        assertEquals(product.imageUrlList[0].id.url, "imageUrl1")
    }

    @Test
    fun addHashTag() {
        //given
        val product = productRepository.getReferenceById(1L)
        val hashTag = hashTagRepository.getReferenceById("PS5")

        //when
        product.addHashTag(hashTag)

        assertEquals(product.productHashTagList[0].tag, hashTag)
    }
}