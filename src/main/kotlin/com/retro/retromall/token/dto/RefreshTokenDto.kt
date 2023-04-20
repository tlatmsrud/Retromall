package com.retro.retromall.token.dto

data class RefreshTokenDto (
    val refreshToken: String,
    val expirationRefreshToken: Long
)