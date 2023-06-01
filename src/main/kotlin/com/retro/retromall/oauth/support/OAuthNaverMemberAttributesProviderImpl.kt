package com.retro.retromall.oauth.support

import com.retro.retromall.oauth.client.dto.OAuthMemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.oauth.client.dto.naver.NaverUserInfoResponse
import org.springframework.stereotype.Component

@Component
class OAuthNaverMemberAttributesProviderImpl : OAuthMemberAttributesProvider<NaverUserInfoResponse> {

    override fun createOAuthMemberAttributes(data: NaverUserInfoResponse): OAuthMemberAttributes {
        return OAuthMemberAttributes(
            OAuthType.NAVER,
            data.naverAccount?.id.toString(),
            data.naverAccount?.name,
            data.naverAccount?.nickname,
            data.naverAccount?.email,
            data.naverAccount?.profileImageUrl
        )
    }
}