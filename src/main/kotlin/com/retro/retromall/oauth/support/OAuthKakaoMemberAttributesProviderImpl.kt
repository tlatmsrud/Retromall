package com.retro.retromall.oauth.support

import com.retro.retromall.oauth.client.dto.OAuthMemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.oauth.client.dto.kakao.KakaoUserInfoResponse
import org.springframework.stereotype.Component

@Component
class OAuthKakaoMemberAttributesProviderImpl : OAuthMemberAttributesProvider<KakaoUserInfoResponse> {
    override fun createOAuthMemberAttributes(data: KakaoUserInfoResponse): OAuthMemberAttributes {
        val kakaoAccount = data.kakaoAccount!!
        val profile = kakaoAccount.profile!!

        return OAuthMemberAttributes(
            oAuthType = OAuthType.KAKAO,
            oauthId = data.id.toString(),
            name = kakaoAccount.name,
            nickName = profile.nickName,
            image = profile.profileImageUrl,
            email = kakaoAccount.email
        )
    }
}