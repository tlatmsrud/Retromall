package com.retro.retromall.member.support

import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.infra.repository.MemberRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class MemberFactory(
    private val memberRepository: MemberRepository
) {
    fun addMemberByOAuthMemberAttributes(attributes: OAuthMemberAttributes): Member {
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