package com.retro.retromall.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.controller.ProductController
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.dto.ProductUpdateRequest
import com.retro.retromall.product.service.ProductService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@WebMvcTest(ProductController::class)
@ActiveProfiles("local")
@ExtendWith(RestDocumentationExtension::class)
class ProductControllerTest {

    private lateinit var objectMapper : ObjectMapper

    @MockBean
    lateinit var productService : ProductService

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private lateinit var mockMvc: MockMvc


    private fun generateValidCreateRequest() : CreateProductRequest {
        return CreateProductRequest(
            "title", "content", 1, "category", "thumbnail", setOf("image"), setOf("hashTag"),1111000000
        )
    }

    private fun generateInvalidCreateRequest() : CreateProductRequest {
        return CreateProductRequest(
            "", "content", 1, "category", "thumbnail", setOf("image"), setOf("hashTag"), 1111000000
        )
    }

    private fun generateValidUpdateRequest() : ProductUpdateRequest {
        return ProductUpdateRequest(
            "Update Product", "content", 1, "category", "thumbnail", setOf("image"), setOf("hashTag"), 1111000000
        )
    }

    private fun authenticationAttributesBuild() : AuthenticationAttributes {
        return AuthenticationAttributes(
            id = 1000L,
            roles = "USER",
            permissions = "CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT"
        )
    }

    @BeforeEach
    fun setUp(webApplicationContext : WebApplicationContext,
              restDocumentationContextProvider : RestDocumentationContextProvider
    ){
        this.objectMapper = ObjectMapper()

        var authenticationAttributes = authenticationAttributesBuild()

        val filter = CharacterEncodingFilter("UTF-8",true)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentationContextProvider))
            .addFilter<DefaultMockMvcBuilder?>(filter)
            .build()

        given(productService.createProduct(authenticationAttributes, generateValidCreateRequest()))
            .willReturn(1L)

        given(productService.updateProduct(authenticationAttributes, 1, generateValidUpdateRequest()))
            .willReturn(1L)

        given(productService.updateProduct(authenticationAttributes, 100, generateValidUpdateRequest()))
            .willThrow(IllegalArgumentException("해당 상품을 찾을 수 없습니다."))

        willDoNothing().given(productService).deleteProduct(authenticationAttributes, 1);

        given(productService.deleteProduct(authenticationAttributes, 100))
            .willThrow(IllegalArgumentException("해당 상품을 찾을 수 없습니다."))

    }

    @Test
    @DisplayName("유효한 요청값을 통한 상품등록")
    fun createProductByValidRequest(){
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products")
                .header("Authorization","Bearer TestToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateValidCreateRequest()))
            )
            .andExpect(status().isOk)
            .andExpect(content().string("1"))
            .andDo(document("createProductByValidRequest"))

        verify(productService).createProduct(
            any(AuthenticationAttributes::class.java), any(CreateProductRequest::class.java))

    }

    @Test
    fun createProductByInvalidRequest(){
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products")
                .header("Authorization","Bearer TestToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateInvalidCreateRequest()))
        )
            .andExpect(status().isOk)
            .andExpect(content().string("{\"message\":\"제목이 없습니다. 제목을 입력해주세요.\"}"))
            .andDo(document("createProductByInvalidRequest"))
    }

    @Test
    @DisplayName("유효한 요청값을 통한 제품수정")
    fun updateProductByValidRequest(){
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/products/{id}",1)
                .header("Authorization","Bearer TestToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateValidUpdateRequest()))
        )
            .andExpect(status().isOk)
            .andDo(document("updateProductByValidRequest"))

        verify(productService).updateProduct(
            any(AuthenticationAttributes::class.java), eq(1L), any(ProductUpdateRequest::class.java))
    }

    @Test
    @DisplayName("유효하지 않은 ID를 통한 제품수정")
    fun updateProductByInvalidId(){
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/products/{id}",100)
                .header("Authorization","Bearer TestToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateValidUpdateRequest()))
        )
            .andExpect(status().isOk)
            .andExpect(content().string("{\"message\":\"해당 상품을 찾을 수 없습니다.\"}"))
            .andDo(document("updateProductByInvalidId"))

        verify(productService).updateProduct(
            any(AuthenticationAttributes::class.java), eq(100L), any(ProductUpdateRequest::class.java))
    }

    @Test
    @DisplayName("유효한 ID에 대한 제품 삭제")
    fun deleteProductByValidId(){
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/products/{id}",1)
                .header("Authorization","Bearer TestToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(document("deleteProductByValidId"))

        verify(productService).deleteProduct(
            any(AuthenticationAttributes::class.java), eq(1L))
    }

    @Test
    @DisplayName("유효하지 않은 ID에 대한 제품 삭제")
    fun deleteProductByInvalidId(){
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/products/{id}",100)
                .header("Authorization","Bearer TestToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("{\"message\":\"해당 상품을 찾을 수 없습니다.\"}"))
            .andDo(document("deleteProductByInvalidId"))

        verify(productService).deleteProduct(
            any(AuthenticationAttributes::class.java), eq(100L))
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
}