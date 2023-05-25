package com.retro.retromall.service

import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.service.ProductLikeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
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
    //@Test
/*    fun 좋아요추가() {
        val product = Product("Title",content = "Product", amount = 10000, category = "Xbox", authorId = 1)
        productRepository.save(product)

        val authenticationAttributes = AuthenticationAttributes(1L)
        productLikeService.addProductLike(authenticationAttributes, product.id!!)

        val updatedProduct = productRepository.findById(product.id!!).orElse(null)
        assertEquals("좋아요 개수", 1, updatedProduct.productLikes.size)
        assertEquals("좋아요 한 유저 아이디", authenticationAttributes.id, updatedProduct.productLikes.first().memberId)
    }*/

}