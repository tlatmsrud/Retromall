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
    fun registRefreshTokenWithMember(memberId: Long, tokenDto: TokenDto) {

        tokenRepository.save(Token(memberId , tokenDto.refreshToken, tokenDto.expirationRefreshToken))
    }

    /**
     * 엑세스 토큰을 갱신한다.
     * 리프레시 토큰에 매핑된 계정에 대한 액세스 토큰을 갱신한다.
     * isRegistRefreshToken 메서드에 따라 리프레시 토큰을 갱신한다.
     *
     * @author sim
     *
     * @param refreshToken - 리프레시 토큰
     * @throws IllegalArgumentException - 유효하지 않은 리프레시 토큰일 경우 발생
     * @return 토큰 객체
     */
    fun renewAccessToken(refreshToken: String): TokenDto {

        val token = tokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow { throw IllegalArgumentException("유효하지 않는 토큰입니다. 로그인을 다시 시도해주세요.") }

        if(token.expirationRefreshToken < Date().time){
            throw IllegalArgumentException("유효기간이 지난 토큰입니다. 로그인을 다시 시도해주세요.")
        }

        /** TODO
         * 리프레시 토큰을 상황에 따라 갱신하여 Set 하기 위해서는 현재 tokenDto로는 불가능.
         * 1. generateToken 메서드를 실행하면 refreshToken과 accessToken을 모두 갱신하고있는 문제.
         * 2. db에 저장된 refreshToken과 쿠키에 저장하는 refreshToken이 달라지게 되는 문제
         * 
         * 해결방안
         * 1. generateToken 메서드를 액세스 토큰, 리프레시 토큰 생성 메서드로 따로 분리
         * 2. 액세스 생성 및 리프레시 토큰 갱신 여부에 따라 리프레시 토큰도 생성
         * 3. 액세스 토큰만 갱신할 경우 refreshToken 정보는 token 엔티티에서 가져와 tokenDto 객체 생성
         * 4. 액세스 토큰과 리프레시 토큰을 갱신할 경우 갱신된 정보로 tokenDto 객체 생성
         */
        memberRepository.selectPermissionsByMemberId(token.memberId!!) ?.let {
            val tokenDto = jwtTokenProvider.generateToken(it)

            if(isRegistRefreshToken(token.memberId)){
                token.updateRefreshToken(tokenDto.refreshToken)
            }
            return tokenDto
        } ?: throw IllegalArgumentException()
    }


    /**
     * 리프레시 토큰 갱신 여부를 체크한다.
     * memberId에 대한 리프레시 토큰이 없거나 만료 기간이 7일 이하로 남아있을 경우 갱신 여부를 true로 리턴한다
     * @author sim
     * @param memberId
     * @return 리프레시 토큰 갱신 여부
     */
    fun isRegistRefreshToken(memberId: Long) : Boolean{

        val token = tokenRepository.findByMemberId(memberId)

        if(token.isEmpty ||
            token.get().expirationRefreshToken < Date().time + toLong(86400000 * 7)){
            return true
        }
        return false
    }
}

