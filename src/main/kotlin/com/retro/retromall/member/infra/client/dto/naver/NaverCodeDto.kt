package com.retro.retromall.member.infra.client.dto.naver

import com.retro.retromall.member.infra.client.dto.OAuthTokenRequest

data class NaverCodeDto(
    val code: String?,
    val state: String?,
    val error: String?,
    val errorDescription: String?
) : OAuthTokenRequest()
