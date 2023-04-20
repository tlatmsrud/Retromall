package com.retro.retromall.token.dto

data class AccessTokenDto (
    val grantType: String,
    val accessToken: String,
    val expirationAccessToken: Long
)