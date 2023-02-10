package com.retro.retromall.member.dto

data class TokenInfo(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)
