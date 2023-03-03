package com.retro.retromall.member.support

import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.client.dto.kakao.KakaoUserInfoResponse
import org.springframework.stereotype.Component

@Component
class OAuthMemberAttributeFactory {
    companion object {
        fun createOauthMemberAttributes(oAuthType: OAuthType, response: Any): OAuthMemberAttributes {
            when (oAuthType) {
                OAuthType.KAKAO -> return createByKakao(response as KakaoUserInfoResponse)
                else -> throw IllegalArgumentException("알 수 없는 OAuthType 입니다.")
            }
        }

        private fun createByKakao(response: KakaoUserInfoResponse): OAuthMemberAttributes {
            val kakaoAccount = response.kakaoAccount!!
            val profile = kakaoAccount.profile!!
            return OAuthMemberAttributes(
                oAuthType = OAuthType.KAKAO,
                oauthId = response.id.toString(),
                name = kakaoAccount.name,
                nickName = profile.nickName,
                image = profile.profileImageUrl,
                email = kakaoAccount.email
            )
        }
    }


}