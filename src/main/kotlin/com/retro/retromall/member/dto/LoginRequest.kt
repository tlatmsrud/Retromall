package com.retro.retromall.member.dto

import com.retro.retromall.member.enums.OAuthType

data class LoginRequest(
    val oAuthType: OAuthType,
    val authorizationCode: String,
    val state: String?,
)
