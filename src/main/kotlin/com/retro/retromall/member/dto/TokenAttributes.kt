package com.retro.retromall.member.dto

data class TokenAttributes(
    var grantType: String? = null,
    var accessToken: String? = null,
    var expiredAccessToken: Long? = null,
    var expiredRefreshToken: Long? = null
)