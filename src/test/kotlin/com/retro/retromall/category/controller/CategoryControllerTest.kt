package com.retro.retromall.category.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.ApiDocumentUtils.Companion.getDocumentRequest
import com.retro.ApiDocumentUtils.Companion.getDocumentResponse
import com.retro.common.JwtTokenProvider
import com.retro.retromall.category.dto.CategoryListResponse
import com.retro.retromall.category.service.CategoryReadService
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter


@WebMvcTest(CategoryController::class)
@ActiveProfiles("local")
@ExtendWith(RestDocumentationExtension::class)
class CategoryControllerTest{
    private lateinit var objectMapper : ObjectMapper

    @MockBean
    lateinit var categoryReadService : CategoryReadService

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private lateinit var mockMvc: MockMvc

    private lateinit var categoryListResponse : CategoryListResponse

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

        setCategoryListResponse()

        given(categoryReadService.getCategoryList()).willReturn(
            categoryListResponse
        )
    }

    private fun setCategoryListResponse(){


        val CT_XBOXLowerCategoryList = listOf(
            CategoryListResponse.Data("CT_XBOX360","Xbox 360 Series", emptyList()),
            CategoryListResponse.Data("CT_XBOXOne","Xbox One Series", emptyList()),
        )

        val CT_XBOX = CategoryListResponse.Data("CT_XBOX","Xbox", CT_XBOXLowerCategoryList)

        val CT_PSLowerCategoryList = listOf(
            CategoryListResponse.Data("CT_PS2","PS2", emptyList()),
            CategoryListResponse.Data("CT_PS3","PS3", emptyList()),
            CategoryListResponse.Data("CT_PS4","PS4", emptyList()),
            CategoryListResponse.Data("CT_PS5","PS5", emptyList()),
        )
        val CT_PS = CategoryListResponse.Data("CT_PS","Play Station", CT_PSLowerCategoryList)

        categoryListResponse = CategoryListResponse(
            listOf(
                CT_PS
                ,CT_XBOX
            )
        )
    }
    @Test
    @DisplayName("카테고리 리스트 조회")
    fun getCategoryList(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/categories")
        )
            .andExpect(status().isOk)
            .andExpect(content().string(CoreMatchers.containsString("CT_PS")))
            .andDo(
                MockMvcRestDocumentation.document(
                    "getCategoryList",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    responseFields(
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("카테고리 리스트"),
                        fieldWithPath("data[].id").type(JsonFieldType.STRING).description("카테고리 ID"),
                        fieldWithPath("data[].category").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("data[].lowerCategories").type(JsonFieldType.ARRAY).description("하위 카테고리 리스트"),
                        fieldWithPath("data[].lowerCategories[].id").type(JsonFieldType.STRING).description("하위 카테고리 리스트"),
                        fieldWithPath("data[].lowerCategories[].category").type(JsonFieldType.STRING).description("하위 카테고리 리스트"),
                        fieldWithPath("data[].lowerCategories[].lowerCategories").type(JsonFieldType.ARRAY).description("하위 카테고리 리스트"),
                     )
                )
            )

        verify(categoryReadService).getCategoryList()
    }
}