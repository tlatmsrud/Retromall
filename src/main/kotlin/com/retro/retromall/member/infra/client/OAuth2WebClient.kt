package com.retro.retromall.member.infra.client

import com.retro.retromall.member.dto.LoginRequest
import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType

interface OAuth2WebClient {
    fun getClient(): OAuth2WebClient
    fun getOAuthType(): OAuthType
    fun requestOAuthToken(loginRequest: LoginRequest): OAuthTokenAttributes
    fun requestOAuthUserInfo(attributes: OAuthTokenAttributes): OAuthMemberAttributes
}