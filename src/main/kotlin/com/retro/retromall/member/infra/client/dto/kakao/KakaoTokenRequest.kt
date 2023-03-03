package com.retro.retromall.member.infra.client.dto.kakao

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoTokenRequest(
    @field:JsonProperty("grant_type")
    val grantType: String,

    @field:JsonProperty("client_id")
    val clientId: String,

    @field:JsonProperty("redirect_uri")
    val redirectUri: String,

    @field:JsonProperty("code")
    val code: String,

    @field:JsonProperty(value = "client_secret")
    val clientSecret: String
)