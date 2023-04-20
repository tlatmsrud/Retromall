package com.retro.retromall.member.service

import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.dto.TokenAttributes
import com.retro.retromall.member.infra.client.dto.OAuthMemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.client.dto.OAuthTokenRequest
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.member.support.OAuth2WebClientFactory
import com.retro.retromall.token.service.TokenService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberReadService(
    private val oAuth2WebClientFactory: OAuth2WebClientFactory,
    private val memberRepository: MemberRepository,
    private val memberService: MemberService,
    private val tokenService: TokenService
) {
    @Transactional
    fun findMemberByOAuth(oAuthType: OAuthType, oAuthTokenRequest: OAuthTokenRequest): LoginResponse {
        val webClient = oAuth2WebClientFactory.getOAuth2WebClient(oAuthType)

        val oAuthTokenAttributes = webClient.getAccessToken(oAuthTokenRequest)
        val oAuthMemberAttributes = webClient.getUserInfo(oAuthTokenAttributes)

        val member = findMemberOrCreateMemberByOAuthMemberAttributes(oAuthMemberAttributes)

        val memberAttributes = memberRepository.selectPermissionsByMemberId(member.id!!)
        val tokenDto = tokenService.generateToken(memberAttributes!!)

        return LoginResponse(
            tokenDto.refreshToken, memberAttributes, TokenAttributes(tokenDto.grantType, tokenDto.accessToken, tokenDto.expirationAccessToken)
        )
    }

    private fun findMemberOrCreateMemberByOAuthMemberAttributes(oAuthMemberAttributes: OAuthMemberAttributes): Member {
        return memberRepository.findByOauthId(oAuthMemberAttributes.oauthId)
            ?: memberService.createMember(oAuthMemberAttributes)
    }

    internal fun findMemberByOAuthId(oauthId: String): Member? {
        return memberRepository.findByOauthId(oauthId)
    }
}