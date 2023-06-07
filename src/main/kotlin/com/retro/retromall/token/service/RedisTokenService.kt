package com.retro.retromall.token.service

import com.retro.aop.JwtTokenProvider
import com.retro.exception.UnauthorizedAccessException
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.repository.MemberRepository
import com.retro.retromall.token.dto.TokenDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.util.*

/**
 * Service For Token
 *
 * @author sim
 */
@Service
@Transactional
@Primary
class RedisTokenService(
    @Value("\${refresh-token-cookie.day}")
    private val refreshTokenCookieDay: Long,
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate : RedisTemplate<String, Long>
) : TokenService {
    /**
     * 토큰을 생성한다.
     * accessToken과 refreshToken을 모두 갱신한다.
     *
     * @author sim
     * @param attributes 유저객체
     * @return 토큰 객체
     */
    override fun generateToken(attributes: MemberAttributes): TokenDto {

        val memberId = attributes.id

        val accessTokenDto = jwtTokenProvider.generateAcesssToken(attributes)

        val refreshTokenDto = jwtTokenProvider.generateRefreshToken(attributes)

        redisTemplate.opsForValue().set(refreshTokenDto.refreshToken, memberId, Duration.ofDays(refreshTokenCookieDay))

        return TokenDto(accessTokenDto.grantType, accessTokenDto.accessToken, refreshTokenDto.refreshToken
                , accessTokenDto.expirationAccessToken, refreshTokenDto.expirationRefreshToken)

    }

    /**
     * 엑세스 토큰을 갱신한다.
     * 기본적으로 액세스 토큰을 갱신하나 설정한 리프레시 토큰의 만료 기간에 따라
     * 리프레시 토큰도 갱신된다.
     *
     * @author sim
     * @param refreshToken - 리프레시 토큰
     * @throws IllegalArgumentException - 유효하지 않은 리프레시 토큰일 경우 발생
     * @return 토큰 객체
     */
    override fun renewAccessToken(refreshToken: String): TokenDto {

        val memberId = getMemberIdByValidRefreshToken(refreshToken)

        memberRepository.selectPermissionsByMemberId(memberId) ?.let {
            return generateToken(it)
        } ?: throw UnauthorizedAccessException("유효하지 않은 리프레시 토큰입니다. 다시 로그인해주세요.")
    }


    /**
     * 리프레시 토큰에 대한 memberId를 조회한다.
     *
     * @author sim
     * @param refreshToken
     * @return MemberId
     * @throws IllegalArgumentException - 기간이 만료되거나 비정상적인 토큰일 경우 예외 발생
     */
    override fun getMemberIdByValidRefreshToken(refreshToken : String) : Long {

        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw UnauthorizedAccessException("위변조된 리프레시 토큰입니다. 다시 로그인해주세요.")
        }

        val memberId = redisTemplate.opsForValue().get(refreshToken) ?:{
            throw UnauthorizedAccessException("유효하지 않은 리프레시 토큰입니다. 다시 로그인해주세요.")
        }

        return memberId as Long
    }
}

