package com.retro.retromall.member.dto

data class LoginResponse(
    val nickName: String?,
    val profileImageUrl: String?,
    val tokenAttributes: TokenAttributes
)
