package com.retro.retromall.member.dto

data class LoginResponse(
    val nickName: String?,
    val tokenAttributes: TokenAttributes
)
