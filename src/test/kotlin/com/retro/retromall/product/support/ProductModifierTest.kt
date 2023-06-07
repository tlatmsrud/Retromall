package com.retro.retromall.product.support

import com.retro.exception.RetromallException
import com.retro.exception.UnauthorizedAccessException
import com.retro.retromall.authorization.enums.Permission
import com.retro.retromall.category.domain.CategoryEntity
import com.retro.retromall.category.service.CategoryReadService
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.ProductEntity
import com.retro.retromall.product.domain.repository.ProductRepository
import com.retro.retromall.product.dto.ProductUpdateRequest
import com.retro.retromall.product.service.ProductAuthorizationService
import com.retro.retromall.product.service.ProductHashTagService
import com.retro.retromall.product.service.ProductImageService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

class ProductModifierTest {
    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var categoryReadService: CategoryReadService

    @Mock
    private lateinit var productImagService: ProductImageService

    @Mock
    private lateinit var productHashTagService: ProductHashTagService

    @Mock
    private lateinit var productAuthorizationService: ProductAuthorizationService
    private lateinit var productModifier: ProductModifier
    private lateinit var product: ProductEntity

    @BeforeEach
    fun `초기화`() {
        MockitoAnnotations.openMocks(this)
        productModifier = ProductModifier(productRepository = productRepository, categoryReadService = categoryReadService, productImageService = productImagService, productHashTagService = productHashTagService, productAuthorizationService = productAuthorizationService)
        product = ProductEntity(title = "Title", content = "content", addressId = 1L, amount = 1000, authorId = 1L, category = "Category")
    }

    @Test
    fun `상품수정(유효한 권한)`() {
        val productId = 1L
        val authenticationAttributes =
            AuthenticationAttributes(1L, "USER", "UPDATE_PRODUCT, DELETE_PRODUCT, CREATE_PRODUCT")
        val dto = ProductUpdateRequest("제목", "내용", 1000, "카테고리", "", setOf(), setOf(), 1L)
        `when`(productRepository.findById(productId)).thenReturn(Optional.of(product))
        `when`(categoryReadService.getCategory(dto.category)).thenReturn(CategoryEntity("새 카테고리", "새 카테고리 아이디"))
        `when`(productAuthorizationService.checkPermission(product, authenticationAttributes, Permission.UPDATE_PRODUCT)).thenCallRealMethod()
        val updateProductId = productModifier.updateProduct(authenticationAttributes, productId, dto)
        assertEquals(productId, updateProductId)
    }

    @Test
    fun `상품수정(유효하지 않은 권한)`() {
        val productId = 1L
        val authenticationAttributes =
            AuthenticationAttributes(2L, "USER", "UPDATE_PRODUCT, DELETE_PRODUCT, CREATE_PRODUCT")
        val dto = ProductUpdateRequest("제목", "내용", 1000, "카테고리", "", setOf(), setOf(), 1L)
        `when`(productRepository.findById(productId)).thenReturn(Optional.of(product))
        `when`(categoryReadService.getCategory(dto.category)).thenReturn(CategoryEntity("새 카테고리", "새 카테고리 아이디"))
        `when`(productAuthorizationService.checkPermission(product, authenticationAttributes, Permission.UPDATE_PRODUCT)).thenCallRealMethod()
        assertThrows<UnauthorizedAccessException> {
            productModifier.updateProduct(authenticationAttributes, productId, dto)
        }
    }
}