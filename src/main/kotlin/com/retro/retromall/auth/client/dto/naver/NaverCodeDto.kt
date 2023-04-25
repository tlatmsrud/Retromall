package com.retro.retromall.auth.client.dto.naver

import com.retro.retromall.auth.client.dto.OAuthAuthorizationCode

data class NaverCodeDto(
    val code: String?,
    val state: String?,
    val error: String?,
    val errorDescription: String?
) : OAuthAuthorizationCode()
