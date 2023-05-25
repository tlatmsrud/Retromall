package com.retro.retromall.product.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.dto.ProductListResponse
import com.retro.retromall.product.dto.ProductResponse
import com.retro.retromall.product.dto.ProductUpdateRequest
import com.retro.retromall.product.service.ProductReadService
import com.retro.retromall.product.service.ProductService
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.SliceImpl
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import java.time.LocalDateTime
import java.util.Date

@WebMvcTest(ProductReadController::class)
@ActiveProfiles("local")
@ExtendWith(RestDocumentationExtension::class)
class ProductReadControllerTest{

    private lateinit var objectMapper : ObjectMapper

    @MockBean
    lateinit var productReadService : ProductReadService

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private lateinit var mockMvc: MockMvc

    private fun authenticationAttributesBuild() : AuthenticationAttributes {
        return AuthenticationAttributes(
            id = 1000L,
            roles = "USER",
            permissions = "CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT"
        )
    }

    private fun generateProductResponse() : ProductResponse{
        return ProductResponse(true, 1, "제품명", "내용", 10000, "author", "PS2",
            0, false, emptySet(), emptySet(), "서울특별시 종로구", LocalDateTime.now(), LocalDateTime.now(),
        )
    }

    private fun generateProductListResponse() : ProductListResponse{
        val content = listOf(
            ProductListResponse.Data( 2, "테스터", "수정제품", 100000, 0, null,
                "서울특별시", LocalDateTime.now(), LocalDateTime.now()),
            ProductListResponse.Data( 1, "테스터1", "수정제품1", 10000, 0, null,
                "서울특별시", LocalDateTime.now(), LocalDateTime.now())
        )

        return ProductListResponse(data = SliceImpl(content, generatePageable(), true))

    }

    private fun generatePageable() : Pageable {
        return PageRequest.of(0,20, Sort.Direction.DESC, "createdAt")
    }

    @BeforeEach
    fun setUp(webApplicationContext : WebApplicationContext,
              restDocumentationContextProvider : RestDocumentationContextProvider
    ){
        this.objectMapper = ObjectMapper()

        var authenticationAttributes = authenticationAttributesBuild()

        val filter = CharacterEncodingFilter("UTF-8",true)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(
                    restDocumentationContextProvider
                )
            )
            .addFilter<DefaultMockMvcBuilder?>(filter)
            .build()

        given(productReadService.getProduct(authenticationAttributes,1)).willReturn(
           generateProductResponse()
        )

        given(productReadService.getProduct(authenticationAttributes,100)).willThrow(
            IllegalArgumentException("요청하신 결과가 없습니다.")
        )

        given(productReadService.getProductList("PS2", generatePageable())).willReturn(
            generateProductListResponse()
        )

    }

    @Test
    @DisplayName("유효 ID에 대한 상품조회")
    fun readProductByValidRequest(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/products/{id}",1)
                .header("Authorization","Bearer TestToken")
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("제품명")))
            .andDo(MockMvcRestDocumentation.document("readProductByValidId"))

        Mockito.verify(productReadService).getProduct(
            any(AuthenticationAttributes::class.java), eq(1L))
    }

    @Test
    @DisplayName("유효하지 않는 ID에 대한 상품조회")
    fun readProductByInvalidRequest(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/products/{id}",100)
                .header("Authorization","Bearer TestToken")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("{\"message\":\"요청하신 결과가 없습니다.\"}"))
            .andDo(MockMvcRestDocumentation.document("readProductByInvalidId"))

        Mockito.verify(productReadService).getProduct(
            any(AuthenticationAttributes::class.java), eq(100L))
    }

    @Test
    @DisplayName("유효 카테고리에 대한 제품 리스트 조회")
    fun getProductListByValidCategory(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/products")
                .header("Authorization","Bearer TestToken")
                .contentType(MediaType.APPLICATION_JSON)
                .param("category", "PS2")
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("수정제품")))
            .andExpect(content().string(containsString("pageable")))
            .andExpect(content().string(containsString("content")))

            .andDo(MockMvcRestDocumentation.document("getProductListByValidCategory"))
    }


    private fun <T> any(type: Class<T>): T = Mockito.any(type)


}