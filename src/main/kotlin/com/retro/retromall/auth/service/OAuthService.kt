package com.retro.retromall.auth.service

import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.auth.client.OAuth2WebClient
import com.retro.retromall.auth.client.dto.OAuthAuthorizationCode
import com.retro.retromall.auth.client.dto.OAuthMemberAttributes
import com.retro.retromall.member.support.OAuth2WebClientFactory
import org.springframework.stereotype.Service

@Service
class OAuthService(
    private val oAuth2WebClientFactory: OAuth2WebClientFactory,
) {
    fun getOAuthMemberAttributes(oAuthType: OAuthType, oAuthAuthorizationCode: OAuthAuthorizationCode): OAuthMemberAttributes {
        val webClient = getWebClient(oAuthType)

        val oAuthTokenAttributes = webClient.getAccessToken(oAuthAuthorizationCode)
        return webClient.getUserInfo(oAuthTokenAttributes)
    }

    private fun getWebClient(oAuthType: OAuthType): OAuth2WebClient {
        return oAuth2WebClientFactory.getOAuth2WebClient(oAuthType)
    }
}