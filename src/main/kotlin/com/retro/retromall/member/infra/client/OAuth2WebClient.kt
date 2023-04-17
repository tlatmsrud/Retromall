package com.retro.retromall.member.infra.client

import com.retro.retromall.member.infra.client.dto.OAuthMemberAttributes
import com.retro.retromall.member.infra.client.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.client.dto.OAuthTokenRequest

interface OAuth2WebClient {
    fun getClient(): OAuth2WebClient
    fun getOAuthType(): OAuthType
    fun getAccessToken(oAuthTokenRequest: OAuthTokenRequest): OAuthTokenAttributes
    fun getUserInfo(attributes: OAuthTokenAttributes): OAuthMemberAttributes
}