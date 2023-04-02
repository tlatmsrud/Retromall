package com.retro.retromall.member.support

import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.client.naver.NaverUserInfoResponse
import org.springframework.stereotype.Component

@Component
class OAuthNaverMemberAttributesProvider : OAuthMemberAttributesProvider {
    override fun createOAuthMemberAttributes(response: Any): OAuthMemberAttributes {
        val naverResponse = response as NaverUserInfoResponse
        return OAuthMemberAttributes(
            OAuthType.NAVER,
            naverResponse.naverAccount?.id.toString(),
            naverResponse.naverAccount?.name,
            naverResponse.naverAccount?.nickname,
            naverResponse.naverAccount?.email,
            naverResponse.naverAccount?.profileImageUrl
        )
    }
}