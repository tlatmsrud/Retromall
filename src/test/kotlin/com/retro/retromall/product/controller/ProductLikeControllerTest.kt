package com.retro.retromall.product.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.ApiDocumentUtils.Companion.getDocumentRequest
import com.retro.ApiDocumentUtils.Companion.getDocumentResponse
import com.retro.aop.JwtTokenProvider
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.service.ProductLikeService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@ActiveProfiles("local")
@WebMvcTest(ProductLikeController::class)
@ExtendWith(RestDocumentationExtension::class)
class ProductLikeControllerTest {

    private lateinit var objectMapper : ObjectMapper

    @MockBean
    lateinit var productLikeService : ProductLikeService

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private lateinit var mockMvc: MockMvc

    private var VALID_PRODUCT_ID = 1L

    private var INVALID_PRODUCT_ID = 100L

    private fun authenticationAttributesBuild() : AuthenticationAttributes {
        return AuthenticationAttributes(
            id = 1000L,
            roles = "USER",
            permissions = "CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT"
        )
    }
    private fun headerBuild(): HttpHeaders {
        val header = HttpHeaders()
        header.set("Authorization","Bearer TestToken")
        return header
    }

    @BeforeEach
    fun setUp(webApplicationContext : WebApplicationContext,
              restDocumentationContextProvider : RestDocumentationContextProvider
    ){
        this.objectMapper = ObjectMapper()

        val authenticationAttributes = authenticationAttributesBuild()

        val filter = CharacterEncodingFilter("UTF-8",true)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(
                    restDocumentationContextProvider
                )
            )
            .addFilter<DefaultMockMvcBuilder?>(filter)
            .build()


        given(productLikeService.addProductLike(authenticationAttributes, INVALID_PRODUCT_ID)).willThrow(
            IllegalArgumentException("해당 상품을 찾을 수 없습니다.")
        )
        given(productLikeService.removeProductLike(authenticationAttributes, INVALID_PRODUCT_ID)).willThrow(
            IllegalArgumentException("해당 상품을 찾을 수 없습니다.")
        )
    }
    @Test
    @DisplayName("유효한 상품 ID에 대한 좋아요 추가")
    fun productLikeAddWithValidProductId() {

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products/like")
                .headers(headerBuild())
                .param("product_id", VALID_PRODUCT_ID.toString())
        )
            .andExpect(status().isOk)
            .andDo(
                document("productLikeAddWithValidProductId"
                    , getDocumentRequest()
                    , getDocumentResponse()
                    , requestParameters(
                        parameterWithName("product_id").description("상품 ID")
                    )
                )
            )

        verify(productLikeService).addProductLike(any(AuthenticationAttributes::class.java), eq(VALID_PRODUCT_ID))
    }

    @Test
    @DisplayName("유효하지 않은 상품 ID에 대한 좋아요 추가")
    fun productLikeAddWithInvalidProductId() {

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products/like")
                .headers(headerBuild())
                .param("product_id", INVALID_PRODUCT_ID.toString())
        )
            .andExpect(status().isOk)
            .andExpect(content().string("{\"message\":\"해당 상품을 찾을 수 없습니다.\"}"))
            .andDo(document("productLikeAddWithInvalidProductId"))

        verify(productLikeService).addProductLike(any(AuthenticationAttributes::class.java), eq(INVALID_PRODUCT_ID))
    }

    @Test
    @DisplayName("유효한 상품 ID에 대한 좋아요 삭제")
    fun productLikeRemoveWithValidProductId() {

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/products/like")
                .headers(headerBuild())
                .param("product_id", VALID_PRODUCT_ID.toString())
        )
            .andExpect(status().isOk)
            .andDo(
                document("productLikeRemoveWithValidProductId"
                    ,requestParameters(
                        parameterWithName("product_id").description("상품 ID")
                    )
                )
            )

        verify(productLikeService).removeProductLike(any(AuthenticationAttributes::class.java), eq(VALID_PRODUCT_ID))
    }

    @Test
    @DisplayName("유효하지 않은 상품 ID에 대한 좋아요 삭제")
    fun productLikeRemoveWithInvalidProductId() {

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/products/like")
                .headers(headerBuild())
                .param("product_id", INVALID_PRODUCT_ID.toString())
        )
            .andExpect(status().isOk)
            .andExpect(content().string("{\"message\":\"해당 상품을 찾을 수 없습니다.\"}"))
            .andDo(document("productLikeRemoveWithInvalidProductId"))

        verify(productLikeService).removeProductLike(any(AuthenticationAttributes::class.java), eq(INVALID_PRODUCT_ID))
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
}