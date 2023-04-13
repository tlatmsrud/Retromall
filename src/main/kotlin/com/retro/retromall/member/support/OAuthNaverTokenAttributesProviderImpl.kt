package com.retro.retromall.member.support

import com.retro.retromall.member.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.client.naver.NaverTokenResponse
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