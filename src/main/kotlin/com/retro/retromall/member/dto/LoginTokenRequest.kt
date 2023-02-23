package com.retro.retromall.member.dto

import com.retro.retromall.member.enums.OAuthType

data class LoginTokenRequest(
    val oAuthType: OAuthType,
    val accessToken: String
)
