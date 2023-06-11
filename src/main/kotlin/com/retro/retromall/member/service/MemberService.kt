package com.retro.retromall.member.service

import com.retro.retromall.oauth.service.OAuthService
import com.retro.retromall.member.domain.MemberEntity
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.dto.TokenAttributes
import com.retro.retromall.oauth.client.dto.OAuthMemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.oauth.client.dto.OAuthAuthorizationCode
import com.retro.retromall.member.repository.MemberRepository
import com.retro.retromall.member.support.MemberFactory
import com.retro.retromall.token.service.TokenService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val oAuthService: OAuthService,
    private val memberRepository: MemberRepository,
    private val memberFactory: MemberFactory,
    private val tokenService: TokenService
) {
    @Transactional
    fun loginMemberWithOAuth(oAuthType: OAuthType, oAuthAuthorizationCode: OAuthAuthorizationCode): LoginResponse {
        val oAuthMemberAttributes = oAuthService.getOAuthMemberAttributes(oAuthType, oAuthAuthorizationCode)

        val member = findMemberOrCreateMemberByOAuthMemberAttributes(oAuthMemberAttributes)

        val memberAttributes = memberRepository.selectPermissionsByMemberId(member.id!!)
        val tokenDto = tokenService.generateToken(memberAttributes!!)

        return LoginResponse(
            tokenDto.refreshToken, memberAttributes, TokenAttributes(tokenDto.grantType, tokenDto.accessToken, tokenDto.expirationAccessToken)
        )
    }

    private fun findMemberOrCreateMemberByOAuthMemberAttributes(oAuthMemberAttributes: OAuthMemberAttributes): MemberEntity {
        return memberRepository.findByOauthId(oAuthMemberAttributes.oauthId)
            ?: createMember(oAuthMemberAttributes)
    }

    private fun createMember(oAuthMemberAttributes: OAuthMemberAttributes): MemberEntity {
        return memberFactory.addMemberByOAuthMemberAttributes(oAuthMemberAttributes)
    }
}