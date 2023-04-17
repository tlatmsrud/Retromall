package com.retro.retromall.member.infra.repository

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
        val query = jpaQueryFactory.select()
            .from(member)
            .innerJoin(memberRole).on(member.id.eq(memberRole.member.id))
            .innerJoin(role).on(memberRole.role.name.eq(role.name))
            .innerJoin(rolePermission).on(role.name.eq(rolePermission.role.name))
            .where(member.id.eq(id))
            .fetch()

        if (query.isNotEmpty()) {
            val member = query[0].get(member)
            val roles = query.stream().map { it.get(role) }.collect(Collectors.toSet())
                .joinToString(separator = ", ") { it?.name.toString() }
            val permissions = query.stream().map { it.get(rolePermission) }.collect(Collectors.toSet())
                .joinToString(separator = ", ") { it?.id!!.permissionName.name }
            return MemberAttributes(member!!.id!!, member.nickname, member.profileImageUrl, roles, permissions)
        }

        return null
    }
}