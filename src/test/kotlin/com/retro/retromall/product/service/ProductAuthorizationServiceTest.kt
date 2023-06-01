package com.retro.retromall.product.service

import com.retro.exception.RetromallException
import com.retro.retromall.authorization.enums.Permission
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.ProductEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ProductAuthorizationServiceTest {
    private lateinit var productAuthorizationService: ProductAuthorizationService
    private lateinit var product: ProductEntity

    @BeforeEach
    fun `초기화`() {
        productAuthorizationService = ProductAuthorizationService()
        product = ProductEntity(
            title = "Title",
            content = "content",
            addressId = 1L,
            amount = 1000,
            authorId = 1L,
            category = "Category"
        )
    }

    @Test
    fun `수정 권한이 있는 경우`() {
        val authenticationAttributes =
            AuthenticationAttributes(1L, "USER", "UPDATE_PRODUCT, DELETE_PRODUCT, CREATE_PRODUCT")
        val permissionType = Permission.UPDATE_PRODUCT

        assertDoesNotThrow { productAuthorizationService.checkPermission(product, authenticationAttributes, permissionType) }
    }

    @Test
    fun `수정 권한이 없는 경우 - 유저 아이디가 일치 하지 않은 경우`() {
        val authenticationAttributes =
            AuthenticationAttributes(2L, "USER", "UPDATE_PRODUCT, DELETE_PRODUCT, CREATE_PRODUCT")
        val permissionType = Permission.UPDATE_PRODUCT

        val exception = assertThrows<RetromallException> { productAuthorizationService.checkPermission(product, authenticationAttributes, permissionType) }

        assertEquals("${permissionType.getMessage()} 권한이 없습니다.", exception.message)
    }

    @Test
    fun `수정 권한이 없는 경우 - 유저 아이디는 일치하나 권한이 없는 경우`() {
        val authenticationAttributes =
            AuthenticationAttributes(1L, "USER", "DELETE_PRODUCT, CREATE_PRODUCT")
        val permissionType = Permission.UPDATE_PRODUCT

        val exception = assertThrows<RetromallException> { productAuthorizationService.checkPermission(product, authenticationAttributes, permissionType) }

        assertEquals("${permissionType.getMessage()} 권한이 없습니다.", exception.message)
    }
}