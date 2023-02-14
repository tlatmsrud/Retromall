package com.retro.retromall.product.service

import com.retro.retromall.category.domain.repository.CategoryRepository
import com.retro.retromall.hashtag.service.HashTagService
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.product.dto.AddProductRequest
import com.retro.retromall.product.domain.Product
import com.retro.retromall.product.domain.ProductImage
import com.retro.retromall.product.domain.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class ProductService(
    private val memberRepository: MemberRepository,
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val hashTagService: HashTagService
) {
    @Transactional
    fun createProduct(memberAttributes: MemberAttributes, dto: AddProductRequest): Long {
        val member =
            memberRepository.findByIdOrNull(memberAttributes.id) ?: throw IllegalStateException("해당 유저를 찾을 수 없습니다.")
        val category = categoryRepository.findById(dto.category)
            .orElseThrow { IllegalArgumentException("존재하지 않는 카테고리입니다.") }
        val hashTags = hashTagService.findOrCreateHashtags(dto.hashTags)
        val product = Product(
            author = member,
            content = dto.content,
            amount = dto.amount,
            category = category
        )
        product.addHashTags(hashTags)

        val images = dto.images.map { ProductImage(imageUrl = it, product) }
        product.addImages(images)

        productRepository.save(product)

        return product.id!!
    }
}