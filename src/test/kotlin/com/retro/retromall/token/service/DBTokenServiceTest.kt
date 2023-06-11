package com.retro.retromall.token.service

import com.google.common.primitives.UnsignedInts.toLong
import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.repository.MemberRepository
import com.retro.retromall.token.domain.Token
import com.retro.retromall.token.domain.repository.TokenRepository
import com.retro.retromall.token.dto.AccessTokenDto
import com.retro.retromall.token.dto.RefreshTokenDto
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.*


class DBTokenServiceTest {

    private val tokenRepository = mock(TokenRepository::class.java)
    private val jwtTokenProvider = mock(JwtTokenProvider::class.java)
    private val memberRepository = mock(MemberRepository::class.java)

    private lateinit var tokenService : DBTokenService

    private val VALID_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"

    private val INVALID_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"invalid"

    private val EXPIRED_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"expired"

    private val ONE_DAY_LEFT_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"one_day_left"

    private val RENEW_ACCESS_TOKEN = "RENEW_ACCESS_TOKEN"

    private val RENEW_REFRESH_TOKEN = "RENEW_ACCESS_TOKEN"

    private val GRANT_TYPE = "Bearer"

    @BeforeEach
    fun setUp(){
        tokenService = DBTokenService(tokenRepository, memberRepository, jwtTokenProvider)

        val token = Token(1, VALID_REFRESH_TOKEN, Date().time + 2592000000)
        val expiredToken = Token(2, EXPIRED_REFRESH_TOKEN,Date().time - toLong(86400000))
        val oneDayLeftToken = Token(3, ONE_DAY_LEFT_REFRESH_TOKEN,Date().time + toLong(86400000))

        val tokenMemberAttributes = MemberAttributes(1,OAuthType.NAVER, "test1@naver.com","테스터1","imgUrl","USER","CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT")
        val expiredTokenMemberAttributes = MemberAttributes(2,OAuthType.NAVER, "test2@naver.com","테스터2","imgUrl","USER","CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT")
        val oneDayLeftTokenMemberAttributes = MemberAttributes(3,OAuthType.NAVER, "test3@naver.com","테스터3","imgUrl","USER","CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT")

        val generatedAccessTokenDto = AccessTokenDto(GRANT_TYPE, RENEW_ACCESS_TOKEN, Date().time + toLong(86400000))
        val generatedRefreshTokenDto = RefreshTokenDto(RENEW_REFRESH_TOKEN, Date().time + 2592000000)

        given(jwtTokenProvider.validateToken(VALID_REFRESH_TOKEN)).willReturn(true)
        given(jwtTokenProvider.validateToken(ONE_DAY_LEFT_REFRESH_TOKEN)).willReturn(true)
        given(jwtTokenProvider.validateToken(INVALID_REFRESH_TOKEN)).willReturn(false)
        given(jwtTokenProvider.validateToken(EXPIRED_REFRESH_TOKEN)).willReturn(false)

        given(tokenRepository.findByRefreshToken(VALID_REFRESH_TOKEN)).willReturn(Optional.of(token))
        given(tokenRepository.findByRefreshToken(INVALID_REFRESH_TOKEN)).willReturn(Optional.empty())
        given(tokenRepository.findByRefreshToken(EXPIRED_REFRESH_TOKEN)).willReturn(Optional.of(expiredToken))
        given(tokenRepository.findByRefreshToken(ONE_DAY_LEFT_REFRESH_TOKEN)).willReturn(Optional.of(oneDayLeftToken))

        given(tokenRepository.findByMemberId(1)).willReturn(token)
        given(tokenRepository.findByMemberId(2)).willReturn(null)
        given(tokenRepository.findByMemberId(3)).willReturn(oneDayLeftToken)

        given(memberRepository.selectPermissionsByMemberId(token.memberId))
            .willReturn(tokenMemberAttributes)

        given(memberRepository.selectPermissionsByMemberId(expiredToken.memberId))
            .willReturn(expiredTokenMemberAttributes)

        given(memberRepository.selectPermissionsByMemberId(oneDayLeftToken.memberId))
            .willReturn(oneDayLeftTokenMemberAttributes)


        given(jwtTokenProvider.generateAcesssToken(any(MemberAttributes::class.java)))
            .willReturn(generatedAccessTokenDto)

        given(jwtTokenProvider.generateRefreshToken(any(MemberAttributes::class.java)))
            .willReturn(generatedRefreshTokenDto)

        given(tokenRepository.save(any(Token::class.java)))
            .will{invocation ->
                invocation.getArgument<Token>(0)
            }
    }


    @Test
    @DisplayName("리프레시 토큰 유효기간 30일 남은 계정에 대한 토큰 생성 / 액세스 토큰만 생성")
    fun generatedToken(){
        val memberAttributes = MemberAttributes(1,OAuthType.NAVER, "test1@naver.com","테스터1","imgUrl","USER","CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT")

        val tokenDto = tokenService.generateToken(memberAttributes)

        assertThat(tokenDto.accessToken).isEqualTo(RENEW_ACCESS_TOKEN)
        assertThat(tokenDto.refreshToken).isEqualTo(VALID_REFRESH_TOKEN)
        
        verify(jwtTokenProvider).generateAcesssToken(memberAttributes)
        verify(tokenRepository).findByMemberId(memberAttributes.id)
    }

    @Test
    @DisplayName("리프레시 토큰 유효기간이 1일 남은 계정에 대한 토큰 생성 / 액세스, 리프레시 토큰 생성")
    fun generatedTokenWithOneDayLeftRefreshToken(){
        val memberAttributes = MemberAttributes(3,OAuthType.NAVER, "test3@naver.com","테스터3","imgUrl","USER","CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT")

        val tokenDto = tokenService.generateToken(memberAttributes)

        assertThat(tokenDto.accessToken).isEqualTo(RENEW_ACCESS_TOKEN)
        assertThat(tokenDto.refreshToken).isEqualTo(RENEW_REFRESH_TOKEN)

        verify(jwtTokenProvider).generateAcesssToken(memberAttributes)
        verify(tokenRepository).findByMemberId(memberAttributes.id)
        verify(jwtTokenProvider).generateRefreshToken(memberAttributes)
        verify(tokenRepository).save(any(Token::class.java))
    }

    @Test
    @DisplayName("유효기간이 30일 남은 리프레시 토큰 / 엑세스토큰만 갱신")
    fun renewAccessTokenByValidToken() {

        val tokenDto = tokenService.renewAccessToken(VALID_REFRESH_TOKEN)

        assertThat(tokenDto.accessToken).isEqualTo(RENEW_ACCESS_TOKEN)
        assertThat(tokenDto.refreshToken).isEqualTo(VALID_REFRESH_TOKEN)

        verify(jwtTokenProvider).validateToken(VALID_REFRESH_TOKEN)
        verify(tokenRepository).findByRefreshToken(VALID_REFRESH_TOKEN)
        verify(memberRepository).selectPermissionsByMemberId(1)
        verify(jwtTokenProvider).generateAcesssToken(any(MemberAttributes::class.java))
        verify(tokenRepository).findByMemberId(1)

    }

    @Test
    @DisplayName("1일 남은 리프레시 토큰 / 엑세스 토큰, 리프레시 토큰 갱신")
    fun renewAccessTokenAndRefreshTokenByOneDayLeftRefreshToken() {
        val tokenDto = tokenService.renewAccessToken(ONE_DAY_LEFT_REFRESH_TOKEN)

        assertThat(tokenDto.accessToken).isEqualTo(RENEW_ACCESS_TOKEN)
        assertThat(tokenDto.refreshToken).isEqualTo(RENEW_REFRESH_TOKEN)


        verify(jwtTokenProvider).validateToken(ONE_DAY_LEFT_REFRESH_TOKEN)
        verify(tokenRepository).findByRefreshToken(ONE_DAY_LEFT_REFRESH_TOKEN)
        verify(memberRepository).selectPermissionsByMemberId(3)
        verify(jwtTokenProvider).generateAcesssToken(any(MemberAttributes::class.java))
        verify(tokenRepository).findByMemberId(3)
        verify(jwtTokenProvider).generateRefreshToken(any(MemberAttributes::class.java))
        verify(tokenRepository).save(any(Token::class.java))
    }

    @Test
    @DisplayName("유효하지 않은 리프레시 토큰 / IllegalArgumentException 예외 발생")
    fun renewAccessTokenByInvalidToken() {
        assertThatThrownBy{ tokenService.renewAccessToken(INVALID_REFRESH_TOKEN) }
            .isInstanceOf(IllegalArgumentException::class.java)

        verify(jwtTokenProvider).validateToken(INVALID_REFRESH_TOKEN)
    }

    @Test
    @DisplayName("유효기간이 지난 리프레시 토큰 / IllegalArgumentException 예외 발생")
    fun renewAccessTokenByExpiredRefreshToken() {
        assertThatThrownBy{ tokenService.renewAccessToken(EXPIRED_REFRESH_TOKEN) }
            .isInstanceOf(IllegalArgumentException::class.java)

        verify(jwtTokenProvider).validateToken(EXPIRED_REFRESH_TOKEN)
    }

    @Test
    @DisplayName("유효한 리프레시 토큰을 통한 토큰 엔티티 조회")
    fun getValidTokenByValidfreshToken(){
        val memberId = tokenService.getMemberIdByValidRefreshToken(VALID_REFRESH_TOKEN)
        assertThat(memberId).isNotNull
        verify(jwtTokenProvider).validateToken(VALID_REFRESH_TOKEN)
    }

    @Test
    @DisplayName("유효하지 않은 리프레시 토큰을 통한 토큰 엔티티 조회")
    fun getValidTokenByInvalidfreshToken(){
        assertThatThrownBy { tokenService.getMemberIdByValidRefreshToken(INVALID_REFRESH_TOKEN) }
            .isInstanceOf(IllegalArgumentException::class.java)

        verify(jwtTokenProvider).validateToken(INVALID_REFRESH_TOKEN)
    }

    @Test
    @DisplayName("유효기간이 만료된 리프레시 토큰을 통한 토큰 엔티티 조회")
    fun getValidTokenByExpiredRefreshToken(){
        assertThatThrownBy { tokenService.getMemberIdByValidRefreshToken(EXPIRED_REFRESH_TOKEN) }
            .isInstanceOf(IllegalArgumentException::class.java)

        verify(jwtTokenProvider).validateToken(EXPIRED_REFRESH_TOKEN)
    }


    @Test
    @DisplayName("유효한 토큰 통한 리프레시 토큰 갱신 여부 체크")
    fun isRegistAndUpdateRefreshTokenWithValidToken(){
        val token = Token(1, VALID_REFRESH_TOKEN, Date().time + 2592000000)
        val result = tokenService.isRegistAndUpdateRefreshToken(token)
        assertThat(result).isEqualTo(false)
    }

    @Test
    @DisplayName("유효기간이 1일 남은 토큰 통한 리프레시 토큰 갱신 여부 체크")
    fun isRegistAndUpdateRefreshTokenWithOneDayLeftToken(){
        val oneDayLeftToken = Token(3, ONE_DAY_LEFT_REFRESH_TOKEN,Date().time + toLong(86400000))
        val result = tokenService.isRegistAndUpdateRefreshToken(oneDayLeftToken)
        assertThat(result).isEqualTo(true)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)

}