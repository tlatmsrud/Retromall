package com.retro.retromall.token.dto


data class TokenDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val expirationAccessToken: Long,
    val expirationRefreshToken: Long
)