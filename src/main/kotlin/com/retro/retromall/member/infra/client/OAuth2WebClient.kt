package com.retro.retromall.member.infra.client

import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.dto.OAuthAttributes
import com.retro.retromall.member.enums.OAuth2Type

interface OAuth2WebClient {
    fun getClient(): OAuth2WebClient
    fun getOAuthType(): OAuth2Type
    fun getToken(authorizationCode: String): OAuthAttributes
    fun getUserInfo(accessToken: String): MemberAttributes
}