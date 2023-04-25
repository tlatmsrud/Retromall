package com.retro.retromall.auth.client.dto.kakao

import com.retro.retromall.auth.client.dto.OAuthAuthorizationCode

data class KakaoCodeDto(
    val code: String?,
    val state: String?,
    val error: String?,
    val errorDescription: String?
) : OAuthAuthorizationCode()
