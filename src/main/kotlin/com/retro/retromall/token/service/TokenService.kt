package com.retro.retromall.token.service

import com.google.common.primitives.UnsignedInts.toLong
import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.token.domain.Token
import com.retro.retromall.token.domain.repository.TokenRepository
import com.retro.retromall.token.dto.TokenDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service For Token
 *
 * @author sim
 */
@Service
@Transactional
class TokenService(
    private val tokenRepository: TokenRepository,
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    /**
     * 토큰을 생성한다.
     * accessToken은 필수적으로 생성한다.
     * refreshToken은 유효기간이 7일 이상 남았을 경우 갱신하지 않는다.
     *
     * @author sim
     * @param attributes 유저객체
     * @return 토큰 객체
     */
    fun generateToken(attributes: MemberAttributes): TokenDto {

        val accessTokenDto = jwtTokenProvider.generateAcesssToken(attributes)

        val token = tokenRepository.findByMemberId(attributes.id)

        if(token == null || isRegistAndUpdateRefreshToken(token)){
            val refreshTokenDto = jwtTokenProvider.generateRefreshToken(attributes)

            val registrationToken = Token(attributes.id, refreshTokenDto.refreshToken, refreshTokenDto.expirationRefreshToken)
            tokenRepository.save(registrationToken)

            return TokenDto(accessTokenDto.grantType, accessTokenDto.accessToken, refreshTokenDto.refreshToken
                , accessTokenDto.expirationAccessToken, refreshTokenDto.expirationRefreshToken)
        }

        return TokenDto(
            accessTokenDto.grantType, accessTokenDto.accessToken, token.refreshToken
            , accessTokenDto.expirationAccessToken, token.expirationRefreshToken)
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
    fun renewAccessToken(refreshToken: String): TokenDto {

        val token = getValidTokenByRefreshToken(refreshToken)

        memberRepository.selectPermissionsByMemberId(token.memberId!!) ?.let {
            return generateToken(it)
        } ?: throw IllegalArgumentException()
    }


    /**
     * 유효한 토큰 엔티티를 리프레시 토큰으로 조회한다.
     *
     * @author sim
     * @param refreshToken
     * @return 유효한 토큰 엔티티
     * @throws IllegalArgumentException - 기간이 만료되거나 비정상적인 토큰일 경우 예외 발생
     */
    fun getValidTokenByRefreshToken(refreshToken : String) : Token {

        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw IllegalArgumentException("유효하지 않은 리프레시 토큰입니다. 다시 로그인해주세요.")
        }

        return tokenRepository.findByRefreshToken(refreshToken).orElseThrow{
            IllegalArgumentException("유효하지 않은 리프레시 토큰입니다. 다시 로그인해주세요.")
        }
    }

    /**
     * 리프레시 토큰 등록/갱신 여부를 체크한다.
     * 토큰 만료기간이 7일 이하로 남아있을 경우 갱신 여부를 true로 리턴한다
     *
     * @author sim
     * @param token
     * @return 리프레시 토큰 갱신 여부
     */
    fun isRegistAndUpdateRefreshToken(token: Token) : Boolean{

        return token.expirationRefreshToken < Date().time + toLong(86400000 * 7)
    }
}

