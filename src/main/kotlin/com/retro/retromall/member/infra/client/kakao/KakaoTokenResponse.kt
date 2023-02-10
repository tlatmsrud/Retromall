package com.retro.retromall.member.infra.client.kakao

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class KakaoTokenResponse(
    @field:JsonProperty("token_type") val tokenType: String,
    @field:JsonProperty("access_token") val accessToken: String,
    @field:JsonProperty("expires_in") val accessTokenExpiresIn: Int,
    @field:JsonProperty("refresh_token") val refreshToken: String,
    @field:JsonProperty("refresh_token_expires_in") val refreshTokenExpiresIn: Int,
    @field:JsonProperty("scope") val scope: String
)