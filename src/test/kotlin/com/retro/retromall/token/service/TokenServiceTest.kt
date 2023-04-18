package com.retro.retromall.token.service

import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.token.domain.Token
import com.retro.retromall.token.domain.repository.TokenRepository
import com.retro.retromall.token.dto.TokenDto
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.*



class TokenServiceTest {

    private val tokenRepository = mock(TokenRepository::class.java)
    private val jwtTokenProvider = mock(JwtTokenProvider::class.java)
    private val memberRepository = mock(MemberRepository::class.java)

    private lateinit var tokenService : TokenService

    private val VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"

    private val INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"invalid"

    private val RENEW_ACCESS_TOKEN = "RENEW_ACCESS_TOKEN"

    private val RENEW_REFRESH_TOKEN = "RENEW_ACCESS_TOKEN"

    private val GRANT_TYPE = "Bearer"
    @BeforeEach
    fun setUp(){
        tokenService = TokenService(tokenRepository, memberRepository, jwtTokenProvider)

        val member = Member(OAuthType.NAVER, "1","tlatmsrud@naver.com","심승경","심드류카네기","imgUrl")
        member.id = 1L

        val token = Token(member, "renewRefreshToken", 100L)
        val memberAttributes = MemberAttributes(1L,OAuthType.NAVER, "tlatmsrud@naver.com","심드류카네기","imgUrl","USER","CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT")

        given(memberRepository.selectPermissionsByMemberId(token.member.id!!))
            .willReturn(memberAttributes)

        given(tokenRepository.findByRefreshToken(VALID_TOKEN))
            .willReturn(Optional.of(token))

        given(tokenRepository.findByRefreshToken(INVALID_TOKEN))
            .willReturn(Optional.empty())

        given(jwtTokenProvider.generateToken(memberAttributes))
            .willReturn(TokenDto(GRANT_TYPE, RENEW_ACCESS_TOKEN, RENEW_REFRESH_TOKEN, 100L, 100L))

        given(tokenRepository.save(any(Token::class.java)))
            .will{invocation ->
                invocation.getArgument<Token>(0)
            }
    }
    @Test
    @DisplayName("유효한 리프레시 토큰을 통한 엑세스 토큰 갱신")
    fun renewAccessTokenByValidToken() {
        val tokenAttributes = tokenService.renewAccessToken(VALID_TOKEN)

        assertThat(tokenAttributes.accessToken).isEqualTo(RENEW_ACCESS_TOKEN)
        assertThat(tokenAttributes.refreshToken).isEqualTo(RENEW_REFRESH_TOKEN)

        verify(tokenRepository).findByRefreshToken(VALID_TOKEN)
        verify(jwtTokenProvider).generateToken(any(MemberAttributes::class.java))
        
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
        val tokenDto = TokenDto(GRANT_TYPE, RENEW_ACCESS_TOKEN, RENEW_REFRESH_TOKEN, 100L, 100L)

        tokenService.registRefreshTokenWithMember(createMember, tokenDto)

        verify(tokenRepository).save(any(Token::class.java))
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)

}