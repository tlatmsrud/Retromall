package com.retro.retromall.member.infra.client.dto.naver

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverTokenRequest(
    @field:JsonProperty("grant_type")
    val grantType: String,

    @field:JsonProperty("client_id")
    val clientId: String,

    @field:JsonProperty("code")
    val code: String,

    @field:JsonProperty(value = "client_secret")
    val clientSecret: String,

    @field:JsonProperty(value = "state")
    val state: String
)