package com.retro.retromall.repository

import com.retro.retromall.category.domain.Category
import com.retro.retromall.category.domain.CategoryRepository
import com.retro.retromall.hashtag.domain.HashTag
import com.retro.retromall.hashtag.domain.repository.HashTagRepository
import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductRepository
import org.junit.jupiter.api.BeforeEach
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
    private var hashTagRepository: HashTagRepository,

    @Autowired
    private val memberRepository: MemberRepository
) {
    @BeforeEach
    fun init() {
        val member = createMember()
        var category: Category = createCategory()
        createHashTag()
        val product =
            Product(
                author = member,
                content = "content",
                amount = 12000,
                category = category,
            )

        productRepository.save(product)
    }

    private fun createMember(): Member {
        return memberRepository.save(Member(OAuthType.KAKAO, "testestest", "", ""))
    }

    private fun createCategory(): Category {
        return categoryRepository.save(Category(name = "PC"))
    }

    private fun createHashTag(): HashTag {
        return hashTagRepository.save(HashTag(tag = "PS5"))
    }
}