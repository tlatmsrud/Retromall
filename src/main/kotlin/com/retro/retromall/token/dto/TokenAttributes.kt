package com.retro.retromall.token.dto

import org.springframework.http.ResponseCookie

data class TokenAttributes(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
){
    /**
     * 리프레시 토큰에 대한 쿠키 객체를 생성한다.
     * 리프레시 토큰에 대한 보안을 위해 https, XSS 공격방지 설정인 secure, httpOnly를 ture로 설정하였다.
     * 토큰 갱신 API(api/token)에서만 리프레시 토큰을 사용하므로 쿠키 경로를 api/token로 한정시켰고, 쿠키의 유효 기간은 30일로 설정하였다.
     * @author sim
     *
     * @return 쿠키 응답객체
     */
    fun generateRefreshTokenCookie() : ResponseCookie {
        return ResponseCookie.from("refresh_token", refreshToken)
            .path("/api/token")
            .secure(true)
            .httpOnly(true)
            .maxAge(60 * 60 * 24 * 30)  // 30 Day
            .build()
    }
}