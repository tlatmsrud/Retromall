package com.retro.retromall.member.support

import com.retro.retromall.auth.client.dto.OAuthTokenAttributes
import com.retro.retromall.auth.client.dto.kakao.KakaoTokenResponse
import org.springframework.stereotype.Component

@Component
class OAuthKakaoTokenAttributesProviderImpl : OAuthTokenAttributesProvider<KakaoTokenResponse> {
    override fun createOAuthTokenAttributes(data: KakaoTokenResponse): OAuthTokenAttributes {
        return OAuthTokenAttributes(
            tokenType = "Bearer",
            accessToken = data.accessToken,
            accessTokenExpiresIn = data.accessTokenExpiresIn,
            refreshToken = data.refreshToken,
            refreshTokenExpiresIn = data.refreshTokenExpiresIn,
            scope = data.scope
        )
    }
}