package com.retro.retromall.controller

import com.retro.common.JwtTokenProvider
import com.retro.retromall.token.TokenController
import com.retro.retromall.token.dto.TokenAttributes
import com.retro.retromall.token.service.TokenService
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import javax.servlet.http.Cookie

@WebMvcTest(TokenController::class)
@AutoConfigureRestDocs
class TokenControllerTest{

    @MockBean
    private lateinit var tokenService : TokenService

    @Autowired lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var ctx: WebApplicationContext

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private var VALID_REFRESH_TOKEN : String = "VALID_REFRESH_TOKEN"

    private var INVALID_REFRESH_TOKEN : String = "INVALID_REFRESH_TOKEN"

    @BeforeEach
    fun setup() {
        val filter = CharacterEncodingFilter("UTF-8",true)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .addFilter<DefaultMockMvcBuilder>(filter)
            .build()

        val tokenAttributes = TokenAttributes("Bearer","newAccessToken","newRefreshToken")

        given(tokenService.renewAccessToken(VALID_REFRESH_TOKEN))
            .willReturn(tokenAttributes)

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
            .andExpect(content().string(containsString("newAccessToken")))

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