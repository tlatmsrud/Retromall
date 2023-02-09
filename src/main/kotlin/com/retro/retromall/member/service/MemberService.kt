package com.retro.retromall.member.service

import com.retro.retromall.common.JwtTokenProvider
import com.retro.retromall.common.TokenInfo
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Transactional
    fun login(memberId: String, password: String): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(memberId, password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        return jwtTokenProvider.generateToken(authentication)
    }

}