package com.retro.retromall.member.support

import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.client.dto.kakao.KakaoUserInfoResponse
import org.springframework.stereotype.Component

@Component
class OAuthKakaoMemberAttributesProvider : OAuthMemberAttributesProvider {
    override fun createOAuthMemberAttributes(response: Any): OAuthMemberAttributes {
        val kakaoResponse = response as KakaoUserInfoResponse
        val kakaoAccount = kakaoResponse.kakaoAccount!!
        val profile = kakaoAccount.profile!!
        return OAuthMemberAttributes(
            oAuthType = OAuthType.KAKAO,
            oauthId = kakaoResponse.id.toString(),
            name = kakaoAccount.name,
            nickName = profile.nickName,
            image = profile.profileImageUrl,
            email = kakaoAccount.email
        )
    }
}