package com.retro.retromall.member.support

import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.infra.client.dto.OAuthMemberAttributes
import com.retro.retromall.member.infra.repository.MemberRepository
import com.retro.retromall.role.enums.Role
import com.retro.retromall.role.repository.RoleRepository
import org.springframework.stereotype.Component

@Component
class MemberFactory(
    private val memberRepository: MemberRepository,
    private val roleRepository: RoleRepository
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
        addUserRole(member)
        return memberRepository.save(member)
    }

    private fun addUserRole(member: Member) {
        val role = roleRepository.findByName(Role.USER) ?: throw IllegalStateException("User Role을 찾을 수 없습니다.")
        member.addRole(role)
    }
}