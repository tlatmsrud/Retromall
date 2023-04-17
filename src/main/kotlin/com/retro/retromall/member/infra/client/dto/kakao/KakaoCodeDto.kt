package com.retro.retromall.member.infra.client.dto.kakao

import com.retro.retromall.member.infra.client.dto.OAuthTokenRequest

data class KakaoCodeDto(
    val code: String?,
    val state: String?,
    val error: String?,
    val errorDescription: String?
) : OAuthTokenRequest()
