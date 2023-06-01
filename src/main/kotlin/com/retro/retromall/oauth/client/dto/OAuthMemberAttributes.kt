package com.retro.retromall.oauth.client.dto

import com.retro.retromall.member.enums.OAuthType

data class OAuthMemberAttributes(
    val oAuthType: OAuthType,
    val oauthId: String,
    val name: String?,
    val nickName: String?,
    val email: String?,
    val image: String?
)
