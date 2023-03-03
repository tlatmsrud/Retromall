package com.retro.retromall.member.service

import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.support.MemberFactory
import com.retro.retromall.member.support.OAuth2WebClientFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val oAuth2WebClientFactory: OAuth2WebClientFactory,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberReadService: MemberReadService,
    private val memberFactory: MemberFactory
) {
    @Transactional
    fun findMemberByOauth(oAuthType: OAuthType, authorizationCode: String): LoginResponse {
        val webClient = oAuth2WebClientFactory.getOAuth2WebClient(oAuthType)
        val oAuthAttributes = webClient.getToken(authorizationCode)
        val memberAttributes = webClient.getUserInfo(oAuthAttributes)
        val member =
            memberReadService.findMemberByOAuthId(memberAttributes.oauthId) ?: memberFactory.addMemberByOAuthMemberAttributes(memberAttributes)
        val tokenAttributes = jwtTokenProvider.generateToken(member)
        return LoginResponse(member.nickname, member.profileImageUrl, tokenAttributes)
    }
}