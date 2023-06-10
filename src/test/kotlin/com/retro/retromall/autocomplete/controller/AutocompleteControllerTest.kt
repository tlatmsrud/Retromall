package com.retro.retromall.autocomplete.controller

import com.retro.ApiDocumentUtils
import com.retro.ApiDocumentUtils.Companion.getDocumentRequest
import com.retro.ApiDocumentUtils.Companion.getDocumentResponse
import com.retro.aop.JwtTokenProvider
import com.retro.retromall.autocomplete.dto.AutocompleteResponse
import com.retro.retromall.autocomplete.service.AutocompleteService
import com.retro.retromall.member.controller.MemberController
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.contains
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter


@WebMvcTest(AutocompleteController::class)
@ExtendWith(RestDocumentationExtension::class)
class AutocompleteControllerTest{

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var autocompleteService : AutocompleteService

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private val searchWord = "플레이스테이션"

    private val autocompleteResponse = AutocompleteResponse(
        listOf("플레이스테이션 2 슬림라인","플레이스테이션 2","플레이스테이션 3","플레이스테이션 4","플레이스테이션 5")
    )


    @BeforeEach
    fun setup(webApplicationContext : WebApplicationContext,
              restDocumentationContextProvider : RestDocumentationContextProvider
    ){
        val filter = CharacterEncodingFilter("UTF-8",true)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(
                    restDocumentationContextProvider
                )
            )
            .addFilter<DefaultMockMvcBuilder?>(filter)
            .build()

        given(autocompleteService.getAutocomplete(searchWord)).willReturn(
            autocompleteResponse
        )
    }

    @Test
    @DisplayName("유효한 검색어를 통한 자동완성 리스트 조회")
    fun getAutocompleteWithValidSearchWord(){
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/autocomplete/{searchWord}",searchWord)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(searchWord)))
            .andDo(
                document(
                    "getAutocompleteWithValidSearchWord",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("searchWord").description("검색어")
                    ),
                    responseFields(
                            fieldWithPath("list").type(JsonFieldType.ARRAY).description("자동완성 리스트"),
                    )
                )
            )
    }


}