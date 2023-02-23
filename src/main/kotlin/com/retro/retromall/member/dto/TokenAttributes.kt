package com.retro.retromall.member.dto

data class TokenAttributes(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
)