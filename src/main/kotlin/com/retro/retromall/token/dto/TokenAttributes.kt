package com.retro.retromall.token.dto

import org.springframework.http.ResponseCookie

data class TokenAttributes(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
){
    fun generateRefreshTokenCookie() : ResponseCookie {
        return ResponseCookie.from("refresh_token", refreshToken)
            .path("/api/token/renew")
            .secure(true)
            .httpOnly(true)
            .maxAge(60 * 60 * 24 * 30)  // 30 Day
            .build()
    }
}