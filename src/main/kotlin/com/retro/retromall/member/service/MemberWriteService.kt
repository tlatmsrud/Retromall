package com.retro.retromall.member.service

import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.support.OAuth2WebClientFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberWriteService(
    private val oAuth2WebClientFactory: OAuth2WebClientFactory,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository,
) {
    //인가코드로 인증
    @Transactional
    fun findMemberByOauth(oAuthType: OAuthType, authorizationCode: String): LoginResponse {
        val webClient = oAuth2WebClientFactory.getOAuth2WebClient(oAuthType)
        val oAuthAttributes = webClient.getToken(authorizationCode)
        val memberAttributes = webClient.getUserInfo(oAuthAttributes)
        val member = findMemberByOAuthAttributes(memberAttributes)
        val tokenAttributes = jwtTokenProvider.generateToken(member)
        return LoginResponse(member.nickname, tokenAttributes)
    }

    @Transactional
    fun findMemberByOAuthAttributes(attributes: OAuthMemberAttributes): Member {
        return memberRepository.findByOauthId(attributes.oauthId).orElse(null)
            ?: return addMember(attributes)
    }

    @Transactional
    fun addMember(attributes: OAuthMemberAttributes): Member {
        val member =
            Member(
                name = attributes.name,
                profileImageUrl = attributes.image,
                nickname = attributes.nickName,
                email = attributes.email,
                oAuthType = attributes.oAuthType,
                oauthId = attributes.oauthId
            )
        return memberRepository.save(member)
    }

}