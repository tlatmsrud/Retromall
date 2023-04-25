package com.retro.retromall.member.support

import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.auth.client.OAuth2WebClient
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class OAuth2WebClientFactory(
    oAuth2WebClients: List<OAuth2WebClient>
) {
    private val oauth2WebClientMap: Map<OAuthType, OAuth2WebClient> = oAuth2WebClients.stream()
        .collect(Collectors.toMap(OAuth2WebClient::getOAuthType, OAuth2WebClient::getClient))

    fun getOAuth2WebClient(oAuthType: OAuthType): OAuth2WebClient {
        return oauth2WebClientMap[oAuthType] ?: throw IllegalArgumentException("지원하지 않는 OAuth 타입 입니다.")
    }
}