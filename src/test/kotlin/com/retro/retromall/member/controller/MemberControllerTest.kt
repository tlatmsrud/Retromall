package com.retro.retromall.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.controller.ProductLikeController
import com.retro.retromall.product.service.ProductLikeService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@ActiveProfiles("local")
@WebMvcTest(ProductLikeController::class)
@ExtendWith(RestDocumentationExtension::class)
class MemberControllerTest {

    private lateinit var objectMapper : ObjectMapper

    @MockBean
    lateinit var productLikeService : ProductLikeService

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(webApplicationContext : WebApplicationContext,
              restDocumentationContextProvider : RestDocumentationContextProvider
    ){
        this.objectMapper = ObjectMapper()

        val filter = CharacterEncodingFilter("UTF-8",true)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(
                    restDocumentationContextProvider
                )
            )
            .addFilter<DefaultMockMvcBuilder?>(filter)
            .build()

    }

    @Test
    @DisplayName("카카오 로그인")
    fun loginKakao() {
    }

    @Test
    @DisplayName("카카오 로그인")
    fun loginNaver() {
    }
}