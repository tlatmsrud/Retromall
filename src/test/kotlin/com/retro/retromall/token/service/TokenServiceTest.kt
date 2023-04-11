package com.retro.retromall.token.service

import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.token.domain.Token
import com.retro.retromall.token.domain.repository.TokenRepository
import com.retro.retromall.token.dto.TokenAttributes
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.web.context.WebApplicationContext
import java.util.*



class TokenServiceTest {

    private val tokenRepository = mock(TokenRepository::class.java)
    private val jwtTokenProvider = mock(JwtTokenProvider::class.java)
    private lateinit var tokenService : TokenService

    private val VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"

    private val INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"invalid"

    private val RENEW_ACCESS_TOKEN = "RENEW_ACCESS_TOKEN"

    private val RENEW_REFRESH_TOKEN = "RENEW_ACCESS_TOKEN"

    private val CREATE_REFRESH_TOKEN = "CREATE_ACCESS_TOKEN"

    private val GRANT_TYPE = "Bearer"
    @BeforeEach
    fun setUp(){
        tokenService = TokenService(tokenRepository, jwtTokenProvider)

        val member = Member(OAuthType.NAVER, "1","tlatmsrud@naver.com","심승경","심드류카네기","imgUrl")
        val token = Token(member, "renewRefreshToken")

        given(tokenRepository.findByRefreshToken(VALID_TOKEN))
            .willReturn(Optional.of(token))

        given(tokenRepository.findByRefreshToken(INVALID_TOKEN))
            .willReturn(Optional.empty())

        given(jwtTokenProvider.generateToken(member))
            .willReturn(TokenAttributes(GRANT_TYPE, RENEW_ACCESS_TOKEN, RENEW_REFRESH_TOKEN))

        given(tokenRepository.save(any(Token::class.java)))
            .will{invocation ->
                invocation.getArgument<Token>(0)
            }
    }
    @Test
    fun renewAccessTokenByValidToken() {
        val tokenAttributes = tokenService.renewAccessToken(VALID_TOKEN)

        assertThat(tokenAttributes.accessToken).isEqualTo(RENEW_ACCESS_TOKEN)
        assertThat(tokenAttributes.refreshToken).isEqualTo(RENEW_REFRESH_TOKEN)

        verify(tokenRepository).findByRefreshToken(VALID_TOKEN)
        verify(jwtTokenProvider).generateToken(any(Member::class.java))
        
    }

    @Test
    fun renewAccessTokenByInvalidToken() {
        assertThatThrownBy{ tokenService.renewAccessToken(INVALID_TOKEN) }
            .isInstanceOf(IllegalArgumentException::class.java)

        verify(tokenRepository).findByRefreshToken(INVALID_TOKEN)

    }

    @Test
    fun registRefreshTokenWithMember(){
        val createMember = Member(OAuthType.NAVER, "3","newUser@naver.com","신규유저","신규유저닉네임","imgUrl")

        tokenService.registRefreshTokenWithMember(createMember, CREATE_REFRESH_TOKEN)

        verify(tokenRepository).save(any(Token::class.java))
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)

}