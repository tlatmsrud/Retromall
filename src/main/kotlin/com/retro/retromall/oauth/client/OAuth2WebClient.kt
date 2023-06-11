package com.retro.retromall.oauth.client

import com.retro.retromall.oauth.client.dto.OAuthMemberAttributes
import com.retro.retromall.oauth.client.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.oauth.client.dto.OAuthAuthorizationCode

interface OAuth2WebClient {
    fun getClient(): OAuth2WebClient
    fun getOAuthType(): OAuthType
    fun getAccessToken(oAuthAuthorizationCode: OAuthAuthorizationCode): OAuthTokenAttributes
    fun getUserInfo(attributes: OAuthTokenAttributes): OAuthMemberAttributes
}