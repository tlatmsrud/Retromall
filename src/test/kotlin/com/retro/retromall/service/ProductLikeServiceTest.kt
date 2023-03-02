package com.retro.retromall.service

import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.service.ProductLikeService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.util.AssertionErrors.assertEquals
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(properties = ["spring.profiles.active=local"])
@TestPropertySource("classpath:application-local.yml")
@AutoConfigureTestDatabase
@Transactional
class ProductLikeServiceTest(
    @Autowired
    private val productLikeService: ProductLikeService,

    @Autowired
    private val productRepository: ProductRepository
) {
    @Test
    fun 좋아요추가() {
        val product = Product(content = "Product", amount = 10000, category = "Xbox", authorId = 1)
        productRepository.save(product)

        val memberAttributes = MemberAttributes(1L)
        productLikeService.addProductLike(memberAttributes, product.id!!)

        val updatedProduct = productRepository.findById(product.id!!).orElse(null)
        assertEquals("좋아요 개수", 1, updatedProduct.productLikes.size)
        assertEquals("좋아요 한 유저 아이디", memberAttributes.id, updatedProduct.productLikes.first().memberId)
    }

}