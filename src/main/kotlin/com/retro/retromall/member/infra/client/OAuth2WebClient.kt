package com.retro.retromall.member.infra.client

import com.retro.retromall.member.dto.LoginRequest
import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType

interface OAuth2WebClient {
    fun getClient(): OAuth2WebClient
    fun getOAuthType(): OAuthType
    fun getAccessToken(loginRequest: LoginRequest): OAuthTokenAttributes
    fun getUserInfo(attributes: OAuthTokenAttributes): OAuthMemberAttributes
}