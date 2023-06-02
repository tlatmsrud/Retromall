package com.retro.retromall.token.service

import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.token.dto.TokenDto

interface TokenService {

    fun generateToken(attributes: MemberAttributes): TokenDto

    fun renewAccessToken(refreshToken: String): TokenDto

    fun getMemberIdByValidRefreshToken(refreshToken : String) : Long
}