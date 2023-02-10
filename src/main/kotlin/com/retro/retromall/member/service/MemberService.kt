package com.retro.retromall.member.service

import com.retro.retromall.member.support.JwtTokenProvider
import com.retro.retromall.member.dto.TokenInfo
import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.enums.OAuth2Type
import com.retro.retromall.member.support.OAuth2WebClientFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val oAuth2WebClientFactory: OAuth2WebClientFactory,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository,
) {
    @Transactional
    fun findMemberByOauth(oAuth2Type: OAuth2Type, authorizationCode: String): TokenInfo {
        val webClient = oAuth2WebClientFactory.getOAuth2WebClient(oAuth2Type)
        val response = webClient.getToken(authorizationCode)
        val oAuthAttributes = webClient.getUserInfo(response.accessToken)
        return jwtTokenProvider.generateToken(findMemberByOAuthAttributes(oAuthAttributes))
    }

    fun findMemberByOAuthAttributes(attributes: MemberAttributes): Member {
        return memberRepository.findByIdOrNull(attributes.oauthId)
            ?: return addMember(attributes)
    }

    @Transactional
    fun addMember(attributes: MemberAttributes): Member {
        val member =
            Member(nickname = attributes.name, email = attributes.email, oauth2Id = attributes.oauthId)
        return memberRepository.save(member)
    }

}