package com.retro.retromall.member.infra.client

import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType

interface OAuth2WebClient {
    fun getClient(): OAuth2WebClient
    fun getOAuthType(): OAuthType
    fun getToken(authorizationCode: String): OAuthTokenAttributes
    fun getUserInfo(attributes: OAuthTokenAttributes): OAuthMemberAttributes
    fun getUserInfoByAccessToken(accessToken: String): OAuthMemberAttributes
}