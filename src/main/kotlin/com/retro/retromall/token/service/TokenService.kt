package com.retro.retromall.token.service

import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.domain.Member
import com.retro.retromall.token.domain.Token
import com.retro.retromall.token.domain.repository.TokenRepository
import com.retro.retromall.token.dto.TokenAttributes
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class TokenService (
    private val tokenRepository: TokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    fun registRefreshTokenWithMember(member : Member, refreshToken : String){
        tokenRepository.save(Token(member, refreshToken))
    }

    fun renewAccessToken(refreshToken: String): TokenAttributes {
        val token = tokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow{ throw IllegalArgumentException("유효하지 않는 토큰입니다. 로그인을 다시 시도해주세요.")}

        val member = token.member
        val tokenAttributes = jwtTokenProvider.generateToken(member)

        token.updateRefreshToken(tokenAttributes.refreshToken)

        return tokenAttributes
    }


}

