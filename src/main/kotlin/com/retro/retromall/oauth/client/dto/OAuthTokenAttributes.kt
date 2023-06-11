package com.retro.retromall.oauth.client.dto

data class OAuthTokenAttributes(
    val tokenType: String,
    val accessToken: String,
    val accessTokenExpiresIn: Int,
    val refreshToken: String,
    val refreshTokenExpiresIn: Int,
    val scope: String?
) {
}