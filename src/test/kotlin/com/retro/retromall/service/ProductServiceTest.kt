package com.retro.retromall.service

import com.retro.retromall.category.domain.Category
import com.retro.retromall.category.domain.CategoryRepository
import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.product.dto.AddProductRequest
import com.retro.retromall.product.domain.ProductRepository
import com.retro.retromall.product.service.ProductService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ProductServiceTest(
    @Autowired
    private val productService: ProductService,

    @Autowired
    private val productRepository: ProductRepository,

    @Autowired
    private val categoryRepository: CategoryRepository,

    @Autowired
    private val memberRepository: MemberRepository

) {
    @BeforeEach
    @Transactional
    fun init() {
        memberRepository.save(Member(OAuthType.KAKAO, "testestest", "", ""))
        categoryRepository.save(Category(name = "PC"))
    }

    @Test
    @Transactional
    fun addProduct() {
        val memberAttributes = MemberAttributes(1L)
        val dto = AddProductRequest(
            content = "",
            amount = 1000,
            category = "PC",
            imageList = mutableListOf(),
            hashTagList = mutableListOf("#PS", "#PC")
        )
        productService.addProduct(memberAttributes, dto)

        val result = productRepository.findById(1L).orElseThrow { IllegalArgumentException("") }

        assertEquals(result.category!!.name, "PC")
        assertEquals(result.hashTags.size, 2)
    }
}