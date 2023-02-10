package com.retro.retromall.member.dto

data class TokenInfo(
    private val grantType: String,
    private val accessToken: String,
    private val refreshToken: String
)
