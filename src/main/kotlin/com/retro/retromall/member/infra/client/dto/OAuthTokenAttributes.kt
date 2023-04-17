package com.retro.retromall.member.infra.client.dto

data class OAuthTokenAttributes(
    val tokenType: String,
    val accessToken: String,
    val accessTokenExpiresIn: Int,
    val refreshToken: String,
    val refreshTokenExpiresIn: Int,
    val scope: String?
) {
}