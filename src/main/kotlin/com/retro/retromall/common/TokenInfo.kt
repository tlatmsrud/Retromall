package com.retro.retromall.common

data class TokenInfo(
    private val grantType: String,
    private val accessToken: String,
    private val refreshToken: String
)
