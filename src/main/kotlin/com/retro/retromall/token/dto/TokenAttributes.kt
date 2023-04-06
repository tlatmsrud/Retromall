package com.retro.retromall.token.dto

data class TokenAttributes(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
)