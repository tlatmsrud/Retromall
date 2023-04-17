package com.retro.retromall.member.service

import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.infra.client.dto.OAuthMemberAttributes
import com.retro.retromall.member.support.MemberFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberService(
    private val memberFactory: MemberFactory,
) {
    fun createMember(oAuthMemberAttributes: OAuthMemberAttributes): Member {
        return memberFactory.addMemberByOAuthMemberAttributes(oAuthMemberAttributes)
    }
}