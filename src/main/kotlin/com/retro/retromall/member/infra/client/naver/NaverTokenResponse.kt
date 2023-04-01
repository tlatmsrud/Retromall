package com.retro.retromall.member.infra.client.naver

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class NaverTokenResponse(
    @field:JsonProperty("token_type") val tokenType: String,
    @field:JsonProperty("access_token") val accessToken: String,
    @field:JsonProperty("expires_in") val expires_in: Int,
    @field:JsonProperty("refresh_token") val refreshToken: String,
    @field:JsonProperty("error") val error: String?,
    @field:JsonProperty("error_description") val errorDescription: String?
)