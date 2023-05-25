package com.retro.retromall.member.support

import com.retro.retromall.member.domain.MemberEntity
import com.retro.retromall.auth.client.dto.OAuthMemberAttributes
import com.retro.retromall.member.repository.MemberRepository
import com.retro.retromall.authorization.enums.Role
import com.retro.retromall.authorization.repository.RoleRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class MemberFactory(
    private val memberRepository: MemberRepository,
    private val roleRepository: RoleRepository
) {
    fun addMemberByOAuthMemberAttributes(attributes: OAuthMemberAttributes): MemberEntity {
        val memberEntity =
            MemberEntity(
                name = attributes.name,
                profileImageUrl = attributes.image,
                nickname = attributes.nickName,
                email = attributes.email,
                oAuthType = attributes.oAuthType,
                oauthId = attributes.oauthId
            )

        memberRepository.save(memberEntity)
        addUserRole(memberEntity)
        return memberEntity
    }

    private fun addUserRole(memberEntity: MemberEntity) {
        val role = roleRepository.findByName(Role.USER) ?: throw IllegalStateException("User Role을 찾을 수 없습니다.")
        memberEntity.addRole(role)
    }
}