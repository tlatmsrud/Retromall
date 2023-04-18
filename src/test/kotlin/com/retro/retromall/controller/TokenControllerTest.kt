package com.retro.retromall.controller

import com.retro.common.JwtTokenProvider
import com.retro.retromall.token.TokenController
import com.retro.retromall.token.dto.TokenDto
import com.retro.retromall.token.service.TokenService
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeEach
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
    private lateinit var tokenService : TokenService

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

        val tokenDto = TokenDto("Bearer",NEW_ACCESS_TOKEN,NEW_REFRESH_TOKEN,1000L,1000L)

        given(tokenService.renewAccessToken(VALID_REFRESH_TOKEN))
            .willReturn(tokenDto)

        given(tokenService.renewAccessToken(INVALID_REFRESH_TOKEN))
            .willThrow(IllegalArgumentException("유효하지 않는 토큰입니다. 로그인을 다시 시도해주세요."))

        given(tokenService.renewAccessToken(""))
            .willThrow(IllegalArgumentException("유효하지 않는 토큰입니다. 로그인을 다시 시도해주세요."))

    }

    @Test
    fun updateTokenByValidRefreshToken(){
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/token")
                .cookie(Cookie("refresh_token", VALID_REFRESH_TOKEN))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(NEW_ACCESS_TOKEN)))
            .andDo(document("updateToken"))

        verify(tokenService).renewAccessToken(VALID_REFRESH_TOKEN)
    }

    @Test
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