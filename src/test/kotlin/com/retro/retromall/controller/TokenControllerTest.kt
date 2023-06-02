package com.retro.retromall.controller

import com.retro.ApiDocumentUtils.Companion.getDocumentRequest
import com.retro.ApiDocumentUtils.Companion.getDocumentResponse
import com.retro.common.JwtTokenProvider
import com.retro.retromall.token.TokenController
import com.retro.retromall.token.dto.TokenDto
import com.retro.retromall.token.service.RedisTokenService
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import javax.servlet.http.Cookie

@WebMvcTest(TokenController::class)
@ActiveProfiles("local")
@ExtendWith(RestDocumentationExtension::class)
class TokenControllerTest{

    @MockBean
    private lateinit var tokenService : RedisTokenService

    @Autowired lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private var VALID_REFRESH_TOKEN : String = "VALID_REFRESH_TOKEN"

    private var INVALID_REFRESH_TOKEN : String = "INVALID_REFRESH_TOKEN"

    private var NEW_REFRESH_TOKEN = "NEW_REFRESH_TOKEN"

    private var NEW_ACCESS_TOKEN = "NEW_ACCESS_TOKEN"

    @BeforeEach
    fun setup(webApplicationContext : WebApplicationContext
              , restDocumentationContextProvider : RestDocumentationContextProvider) {

        val filter = CharacterEncodingFilter("UTF-8",true)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentationContextProvider))
            .addFilter<DefaultMockMvcBuilder>(filter)
            .build()

        val tokenDto = TokenDto("Bearer",NEW_ACCESS_TOKEN,NEW_REFRESH_TOKEN,1684565900922L,1684585900922L)

        given(tokenService.renewAccessToken(VALID_REFRESH_TOKEN))
            .willReturn(tokenDto)

        given(tokenService.renewAccessToken(INVALID_REFRESH_TOKEN))
            .willThrow(IllegalArgumentException("유효하지 않는 토큰입니다. 로그인을 다시 시도해주세요."))

        given(tokenService.renewAccessToken(""))
            .willThrow(IllegalArgumentException("유효하지 않는 토큰입니다. 로그인을 다시 시도해주세요."))

    }

    @Test
    @DisplayName("유효한 리프레시 토큰을 통한 토큰 갱신")
    fun updateTokenByValidRefreshToken(){
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/token")
                .cookie(Cookie("refresh_token", VALID_REFRESH_TOKEN))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(NEW_ACCESS_TOKEN)))
            .andDo(
                document("updateToken"
                , getDocumentRequest()
                , getDocumentResponse()
                , responseFields(
                        fieldWithPath("grantType").type(JsonFieldType.STRING).description("grant type(Bearer 고정)"),
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
                        fieldWithPath("expirationAccessToken").type(JsonFieldType.NUMBER).description("액세스 토큰 만료기간(Number 타입)"),
                        fieldWithPath("expirationRefreshToken").type(JsonFieldType.NUMBER).description("리프레시 토큰 만료기간(Number 타입)")
                    )
                )
            )

        verify(tokenService).renewAccessToken(VALID_REFRESH_TOKEN)
    }

    @Test
    @DisplayName("유요하지 않은 리프레시 토큰을 통한 토큰 갱신")
    fun updateTokenByInvalidRefreshToken(){
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/token")
                .cookie(Cookie("refresh_token", INVALID_REFRESH_TOKEN))
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("유효하지 않는 토큰입니다. 로그인을 다시 시도해주세요.")))

        verify(tokenService).renewAccessToken(INVALID_REFRESH_TOKEN)
    }

    @Test
    @DisplayName("빈 리프레시 토큰을 통한 토큰 갱신")
    fun updateTokenByEmptyRefreshToken(){
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/token")
                .cookie(Cookie("refresh_token", ""))
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("유효하지 않는 토큰입니다. 로그인을 다시 시도해주세요.")))

        verify(tokenService).renewAccessToken("")
    }

}