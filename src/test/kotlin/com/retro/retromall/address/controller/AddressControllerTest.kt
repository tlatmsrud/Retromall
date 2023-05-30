package com.retro.retromall.address.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.ApiDocumentUtils
import com.retro.ApiDocumentUtils.Companion.getDocumentRequest
import com.retro.ApiDocumentUtils.Companion.getDocumentResponse
import com.retro.common.JwtTokenProvider
import com.retro.retromall.address.domain.AddressEntity
import com.retro.retromall.address.service.AddressService
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@WebMvcTest(AddressController::class)
@ActiveProfiles("local")
@ExtendWith(RestDocumentationExtension::class)
class AddressControllerTest{

    private lateinit var objectMapper : ObjectMapper

    @MockBean
    lateinit var addressService : AddressService

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private lateinit var mockMvc: MockMvc

    private val VALID_SEARCH_WORK = "면목동"

    private val INVALID_SEARCH_WORK = "무지개시티동"

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

        BDDMockito.given(addressService.searchAddress(VALID_SEARCH_WORK))
            .willReturn(listOf(
                AddressEntity(1120005200,"서울특별시 성동구 면목동")
                ,AddressEntity(1123000100,"서울특별시 동대문구 면목동")
                ,AddressEntity(1126010100,"서울특별시 중랑구 면목동")))

        BDDMockito.given(addressService.searchAddress(INVALID_SEARCH_WORK))
            .willReturn(emptyList())
    }

    @Test
    @DisplayName("유효한 검색어를 통한 주소검색")
    fun searchAddressByValidSearchWord(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/address/search")
                .header("Authorization","Bearer TestToken")
                .param("searchWord",VALID_SEARCH_WORK)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].addr", containsString("면목동")))
            .andDo(
                document("searchAddressByValidSearchWord"
                    , getDocumentRequest()
                    , getDocumentResponse()
                    , requestParameters(
                            parameterWithName("searchWord").description("검색어")
                    )
                    , responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("주소 ID"),
                        fieldWithPath("[].addr").type(JsonFieldType.STRING).description("주소명")
                )
            ))

        Mockito.verify(addressService).searchAddress(VALID_SEARCH_WORK)
    }

    @Test
    @DisplayName("유효하지 않은 검색어를 통한 주소검색")
    fun createProductByInvalidInRequest(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/address/search")
                .header("Authorization","Bearer TestToken")
                .param("searchWord",INVALID_SEARCH_WORK)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("[]"))
            .andDo(document("searchAddressByInValidSearchWord"))

        Mockito.verify(addressService).searchAddress(INVALID_SEARCH_WORK)
    }
}