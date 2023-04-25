package com.retro.retromall.member.support

import com.retro.retromall.member.domain.Member
import com.retro.retromall.auth.client.dto.OAuthMemberAttributes
import com.retro.retromall.member.repository.MemberRepository
import com.retro.retromall.role.enums.Role
import com.retro.retromall.role.repository.RoleRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
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

        memberRepository.save(member)
        addUserRole(member)
        return member
    }

    private fun addUserRole(member: Member) {
        val role = roleRepository.findByName(Role.USER) ?: throw IllegalStateException("User Role을 찾을 수 없습니다.")
        member.addRole(role)
    }
}