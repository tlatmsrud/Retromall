package com.retro.retromall.member.dto

import com.retro.retromall.member.enums.OAuthType

data class MemberAttributes(
    val id: Long,
    val oauthType: OAuthType,
    val oauthId: String,
    val nickName: String?,
    val profileImageUrl: String?,
    val roles: String?,
    val permissions: String?,
)