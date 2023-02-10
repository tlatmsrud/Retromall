package com.retro.retromall.member.dto

import com.retro.retromall.member.enums.OAuth2Type

data class LoginRequest(
    val oAuth2Type: OAuth2Type,
    val authorizationCode: String
)
