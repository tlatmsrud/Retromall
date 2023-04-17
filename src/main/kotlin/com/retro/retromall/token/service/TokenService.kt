package com.retro.retromall.token.service

import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.token.domain.Token
import com.retro.retromall.token.domain.repository.TokenRepository
import com.retro.retromall.token.dto.TokenDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
     * @author jiho
     * @param attributes 유저객체
     * @return 토큰 객체
     */
    fun generateToken(attributes: MemberAttributes): TokenDto {
        return jwtTokenProvider.generateToken(attributes)
    }

    /**
     * 유저에 대한 리프레시 토큰을 저장한다.
     * @author sim
     *
     * @param member - 유저 정보
     * @param refreshToken - 리프레시 토큰
     */
    fun registRefreshTokenWithMember(member: Member, tokenDto: TokenDto) {
        tokenRepository.save(Token(member, tokenDto.refreshToken, tokenDto.expirationRefreshToken))
    }

    /**
     * 엑세스 토큰을 갱신한다.
     * 리프레시 토큰에 매핑된 계정에 대한 액세스 토큰을 갱신한다.
     * @author sim
     *
     * @param refreshToken - 리프레시 토큰
     * @throws IllegalArgumentException - 유효하지 않은 리프레시 토큰일 경우 발생
     * @return 토큰 객체
     */
    fun renewAccessToken(refreshToken: String): TokenDto {
        val token = tokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow { throw IllegalArgumentException("유효하지 않는 토큰입니다. 로그인을 다시 시도해주세요.") }

        memberRepository.selectPermissionsByMemberId(token.member.id!!) ?.let {
            val tokenAttributes = jwtTokenProvider.generateToken(it)
            token.updateRefreshToken(tokenAttributes.refreshToken)
            return tokenAttributes
        } ?: throw IllegalArgumentException()
    }
}

