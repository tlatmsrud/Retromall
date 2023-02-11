package com.retro.retromall.member.dto

import com.retro.retromall.member.enums.OAuthType

data class LoginAttributes(
    val oAuthType: OAuthType,
    val authorizationCode: String
)
