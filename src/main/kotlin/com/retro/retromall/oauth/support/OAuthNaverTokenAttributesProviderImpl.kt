package com.retro.retromall.oauth.support

import com.retro.retromall.oauth.client.dto.OAuthTokenAttributes
import com.retro.retromall.oauth.client.dto.naver.NaverTokenResponse
import org.springframework.stereotype.Component

@Component
class OAuthNaverTokenAttributesProviderImpl : OAuthTokenAttributesProvider<NaverTokenResponse> {
    override fun createOAuthTokenAttributes(data: NaverTokenResponse): OAuthTokenAttributes {
        return OAuthTokenAttributes(
            tokenType = data.tokenType,
            accessToken = data.accessToken,
            refreshToken = data.refreshToken,
            accessTokenExpiresIn = data.expires_in,
            refreshTokenExpiresIn = 0,
            scope = null
        )
    }
}