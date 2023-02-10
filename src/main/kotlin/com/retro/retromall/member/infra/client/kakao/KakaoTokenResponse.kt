package com.retro.retromall.member.infra.client.kakao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.util.StringUtils
import java.util.*

class KakaoTokenResponse(
    tokenType: String,
    accessToken: String,
    accessTokenExpiresIn: Int,
    refreshToken: String,
    refreshTokenExpiresIn: Int,
    scope: String
) {
    @JsonProperty(value = "token_type", required = true)
    val tokenType = tokenType

    @JsonProperty(value = "access_token", required = true)
    val accessToken = accessToken

    @JsonProperty(value = "expires_in", required = true)
    val accessTokenExpiresIn = accessTokenExpiresIn

    @JsonProperty(value = "refresh_token", required = true)
    val refreshToken = refreshToken

    @JsonProperty(value = "refresh_token_expires_in", required = true)
    val refreshTokenExpiresIn = refreshTokenExpiresIn

    @JsonProperty("scope")
    val scope = scope

    fun getScopeList(): MutableList<String> {
        var result = mutableListOf<String>()
        if (StringUtils.hasText(scope))
            result = scope.split(", ").toMutableList()
        return result
    }

}
