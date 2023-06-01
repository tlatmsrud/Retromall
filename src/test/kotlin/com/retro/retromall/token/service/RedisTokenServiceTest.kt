package com.retro.retromall.token.service

import com.google.common.primitives.UnsignedInts.toLong
import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.repository.MemberRepository
import com.retro.retromall.token.domain.Token
import com.retro.retromall.token.dto.AccessTokenDto
import com.retro.retromall.token.dto.RefreshTokenDto
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willDoNothing
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.time.Duration
import java.util.*


class RedisTokenServiceTest {

    private val redisTemplate = mock(RedisTemplate::class.java)
    private val jwtTokenProvider = mock(JwtTokenProvider::class.java)
    private val memberRepository = mock(MemberRepository::class.java)
    private val valueOperations = mock(ValueOperations::class.java)


    private lateinit var tokenService : RedisTokenService

    private val VALID_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"

    private val INVALID_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"invalid"

    private val EXPIRED_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"expired"

    private val ONE_DAY_LEFT_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODA3OTUwNjB9._c66OVOh0x6VUaHBm3Y4Fyh44oNNsFSGnSFxLM8o3O8"+"one_day_left"

    private val RENEW_ACCESS_TOKEN = "RENEW_ACCESS_TOKEN"

    private val RENEW_REFRESH_TOKEN = "RENEW_ACCESS_TOKEN"

    private val GRANT_TYPE = "Bearer"

    private val REFRESH_TOKEN_COOKIE_DAY = 30L
    @BeforeEach
    fun setUp(){

        tokenService = RedisTokenService(REFRESH_TOKEN_COOKIE_DAY, memberRepository, jwtTokenProvider, redisTemplate as RedisTemplate<String, Long>)

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

        given(redisTemplate.opsForValue()).willReturn(valueOperations as ValueOperations<String, Long>)

        willDoNothing().given(valueOperations).set(any(String::class.java), any(Long::class.java), eq(Duration.ofDays(30)))

        given(valueOperations.get(VALID_REFRESH_TOKEN)).willReturn(1L)

        given(valueOperations.get(INVALID_REFRESH_TOKEN)).willReturn(null)
        given(valueOperations.get(EXPIRED_REFRESH_TOKEN)).willReturn(null)


    }


    @Test
    @DisplayName("액세스, 리프레시 토큰 생성")
    fun generatedToken(){
        val memberAttributes = MemberAttributes(1,OAuthType.NAVER, "test1@naver.com","테스터1","imgUrl","USER","CREATE_PRODUCT, MODIFY_PRODUCT, DELETE_PRODUCT")

        val tokenDto = tokenService.generateToken(memberAttributes)

        assertThat(tokenDto.accessToken).isEqualTo(RENEW_ACCESS_TOKEN)
        assertThat(tokenDto.refreshToken).isEqualTo(RENEW_REFRESH_TOKEN)
        
        verify(jwtTokenProvider).generateAcesssToken(memberAttributes)
        verify(jwtTokenProvider).generateRefreshToken(memberAttributes)

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
    fun getValidTokenByInvalidRefreshToken(){
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



    private fun <T> any(type: Class<T>): T = Mockito.any(type)

}