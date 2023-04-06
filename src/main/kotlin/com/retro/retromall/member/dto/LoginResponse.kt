package com.retro.retromall.member.dto

import com.retro.retromall.token.dto.TokenAttributes

data class LoginResponse(
    val nickName: String?,
    val profileImageUrl: String?,
    val tokenAttributes: TokenAttributes
)
