package com.retro.retromall.member.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.member.domain.QMember.member
import com.retro.retromall.member.domain.QMemberRole.memberRole
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.role.domain.QRole.role
import com.retro.retromall.role.domain.QRolePermission.rolePermission
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class MemberCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : MemberCustomRepository {
    override fun selectPermissionsByMemberId(id: Long): MemberAttributes? {
        val query = jpaQueryFactory.select(member, memberRole.id.roleName, rolePermission.id.permissionName)
            .from(member)
            .innerJoin(memberRole).on(member.id.eq(memberRole.id.memberId))
            .innerJoin(role).on(memberRole.id.roleName.eq(role.name))
            .innerJoin(rolePermission).on(role.name.eq(rolePermission.role.name))
            .where(member.id.eq(id))
            .fetch()

        if (query.isNotEmpty()) {
            val member = query[0].get(member)
            val roles = query.stream().map { it.get(memberRole.id.roleName) }.collect(Collectors.toSet())
                .joinToString(separator = ", ") { it?.name.toString() }
            val permissions = query.stream().map { it.get(rolePermission.id.permissionName) }.collect(Collectors.toSet())
                .joinToString(separator = ", ") { it?.name.toString() }
            return MemberAttributes(member!!.id!!, member.oauthType, member.oauthId, member.nickname, member.profileImageUrl, roles, permissions)
        }

        return null
    }
}