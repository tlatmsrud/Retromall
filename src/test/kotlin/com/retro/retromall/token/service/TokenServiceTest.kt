package com.retro.retromall.token.service

import com.google.common.primitives.UnsignedInts.toLong
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
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import java.util.*


class TokenServiceTest {

    private val tokenRepository = mock(TokenRepository::class.java)
    private val jwtTokenProvider = mock(JwtTokenProvider::class.java)
    private val memberRepository = mock(MemberRepository::class.java)

    private lateinit var tokenService : TokenService

    private val VALID_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"

    private val INVALID_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"invalid"

    private val EXPIRED_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"expired"

    private val ONE_DAY_LEFT_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"one_day_left"

    private val RENEW_ACCESS_TOKEN = "RENEW_ACCESS_TOKEN"

    private val RENEW_REFRESH_TOKEN = "RENEW_ACCESS_TOKEN"

    private val GRANT_TYPE = "Bearer"

    @BeforeEach
    fun setUp(){
        tokenService = TokenService(tokenRepository, memberRepository, jwtTokenProvider)

        val token = Token(1, VALID_REFRESH_TOKEN, Date().time + 2592000000)
        val expiredToken = Token(2, EXPIRED_REFRESH_TOKEN,Date().time - toLong(86400000))
        val oneDayLeftToken = Token(3, ONE_DAY_LEFT_REFRESH_TOKEN,Date().time + toLong(86400000))

        val tokenMemberAttributes = MemberAttributes(1,OAuthType.NAVER, "test1@naver.com","테스터1","imgUrl","USER","CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT")
        val expiredTokenMemberAttributes = MemberAttributes(2,OAuthType.NAVER, "test2@naver.com","테스터2","imgUrl","USER","CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT")
        val oneDayLeftTokenMemberAttributes = MemberAttributes(3,OAuthType.NAVER, "test3@naver.com","테스터3","imgUrl","USER","CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT")

        given(tokenRepository.findByRefreshToken(VALID_REFRESH_TOKEN))
            .willReturn(Optional.of(token))

        given(tokenRepository.findByRefreshToken(INVALID_REFRESH_TOKEN))
            .willReturn(Optional.empty())

        given(tokenRepository.findByRefreshToken(EXPIRED_REFRESH_TOKEN))
            .willReturn(Optional.of(expiredToken))

        given(tokenRepository.findByRefreshToken(ONE_DAY_LEFT_REFRESH_TOKEN))
            .willReturn(Optional.of(oneDayLeftToken))

        given(memberRepository.selectPermissionsByMemberId(token.memberId))
            .willReturn(tokenMemberAttributes)

        given(memberRepository.selectPermissionsByMemberId(expiredToken.memberId))
            .willReturn(expiredTokenMemberAttributes)

        given(memberRepository.selectPermissionsByMemberId(oneDayLeftToken.memberId))
            .willReturn(oneDayLeftTokenMemberAttributes)

        given(jwtTokenProvider.generateToken(any(MemberAttributes::class.java)))
            .willReturn(TokenDto(GRANT_TYPE, RENEW_ACCESS_TOKEN, RENEW_REFRESH_TOKEN, Date().time + 86400000, Date().time + 2592000000))

        given(tokenRepository.findByMemberId(1))
            .willReturn(Optional.of(token))

        given(tokenRepository.save(any(Token::class.java)))
            .will{invocation ->
                invocation.getArgument<Token>(0)
            }
    }


    @Test
    @DisplayName("유효기간이 30일 남은 리프레시 토큰 / 엑세스, 리프레시 토큰 갱신")
    fun renewAccessTokenByValidToken() {
        // TODO : 토큰 서비스에서의 토큰객체는 모
        val tokenDto = tokenService.renewAccessToken(VALID_REFRESH_TOKEN)

        assertThat(tokenDto.accessToken).isEqualTo(RENEW_ACCESS_TOKEN)
        assertThat(tokenDto.refreshToken).isEqualTo(RENEW_ACCESS_TOKEN)

        verify(tokenRepository).findByRefreshToken(VALID_REFRESH_TOKEN)
        verify(memberRepository).selectPermissionsByMemberId(any(Long::class.java))
        verify(jwtTokenProvider).generateToken(any(MemberAttributes::class.java))
    }

    @Test
    @DisplayName("1일 남은 리프레시 토큰 / 엑세스 토큰, 리프레시 토큰 갱신")
    fun renewAccessTokenAndRefreshTokenByOneDayLeftRefreshToken() {
        val tokenDto = tokenService.renewAccessToken(ONE_DAY_LEFT_REFRESH_TOKEN)

        assertThat(tokenDto.accessToken).isEqualTo(RENEW_ACCESS_TOKEN)
        assertThat(tokenDto.refreshToken).isEqualTo(RENEW_REFRESH_TOKEN)

        verify(tokenRepository).findByRefreshToken(ONE_DAY_LEFT_REFRESH_TOKEN)
        verify(memberRepository).selectPermissionsByMemberId(any(Long::class.java))
        verify(jwtTokenProvider).generateToken(any(MemberAttributes::class.java))
    }

    @Test
    @DisplayName("유효하지 않은 리프레시 토큰 / IllegalArgumentException 예외 발생")
    fun renewAccessTokenByInvalidToken() {
        assertThatThrownBy{ tokenService.renewAccessToken(INVALID_REFRESH_TOKEN) }
            .isInstanceOf(IllegalArgumentException::class.java)

        verify(tokenRepository).findByRefreshToken(INVALID_REFRESH_TOKEN)
    }

    @Test
    @DisplayName("유효기간이 지난 리프레시 토큰 / IllegalArgumentException 예외 발생")
    fun renewAccessTokenByExpiredRefreshToken() {
        assertThatThrownBy{ tokenService.renewAccessToken(EXPIRED_REFRESH_TOKEN) }
            .isInstanceOf(IllegalArgumentException::class.java)

        verify(tokenRepository).findByRefreshToken(EXPIRED_REFRESH_TOKEN)
    }

    @Test
    @DisplayName("리프레시 토큰 등록")
    fun registRefreshTokenWithMember(){

        val tokenDto = TokenDto(GRANT_TYPE, RENEW_ACCESS_TOKEN, RENEW_REFRESH_TOKEN, Date().time + toLong(86400000), Date().time + 2592000000)
        val token = Token(1, tokenDto.refreshToken, tokenDto.expirationRefreshToken);

        tokenService.registRefreshTokenWithMember(1, tokenDto)
        verify(tokenRepository).save(token)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)

}