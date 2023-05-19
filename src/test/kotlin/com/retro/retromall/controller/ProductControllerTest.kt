package com.retro.retromall.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.controller.ProductController
import com.retro.retromall.product.dto.CreateProductRequest
import com.retro.retromall.product.service.ProductService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
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


    private fun getValidCreateRequest() : CreateProductRequest {
        return CreateProductRequest(
            "title", "content", 1, "category", "thumbnail", setOf("image"), setOf("hashTag"),1111000000
        )
    }

    private fun getInvalidCreateRequest() : CreateProductRequest {
        return CreateProductRequest(
            "", "content", 1, "category", "thumbnail", setOf("image"), setOf("hashTag"), 1111000000
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
        var validCreateRequest = getValidCreateRequest()

        val filter = CharacterEncodingFilter("UTF-8",true)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentationContextProvider))
            .addFilter<DefaultMockMvcBuilder?>(filter)
            .build()

        given(productService.createProduct(authenticationAttributes, validCreateRequest))
            .willReturn(1L)

    }

    @Test
    fun createProductByValidRequest(){
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products")
                .header("Authorization","Bearer TestToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getValidCreateRequest()))
            )
            .andExpect(status().isOk)
            .andExpect(content().string("1"))

        verify(productService).createProduct(
            any(AuthenticationAttributes::class.java), any(CreateProductRequest::class.java))

    }

    @Test
    fun createProductByInvalidRequest(){
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products")
                .header("Authorization","Bearer TestToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getInvalidCreateRequest()))
        )
            .andExpect(status().isOk)
            .andExpect(content().string("{\"message\":\"제목이 없습니다. 제목을 입력해주세요.\"}"))
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
}