package com.retro.retromall.member.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.retro.retromall.member.domain.QMemberEntity.memberEntity
import com.retro.retromall.member.domain.QMemberRoleEntity.memberRoleEntity
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.authorization.domain.QRoleEntity.roleEntity
import com.retro.retromall.authorization.domain.QRolePermissionEntity.rolePermissionEntity
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class MemberCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : MemberCustomRepository {
    override fun selectPermissionsByMemberId(id: Long): MemberAttributes? {
        val query = jpaQueryFactory.select(memberEntity, memberRoleEntity.id.roleName, rolePermissionEntity.id.permissionName)
            .from(memberEntity)
            .innerJoin(memberRoleEntity).on(memberEntity.id.eq(memberRoleEntity.id.memberId))
            .innerJoin(roleEntity).on(memberRoleEntity.id.roleName.eq(roleEntity.name))
            .innerJoin(rolePermissionEntity).on(roleEntity.name.eq(rolePermissionEntity.roleEntity.name))
            .where(memberEntity.id.eq(id))
            .fetch()

        if (query.isNotEmpty()) {
            val member = query[0].get(memberEntity)
            val roles = query.stream().map { it.get(memberRoleEntity.id.roleName) }.collect(Collectors.toSet())
                .joinToString(separator = ", ") { it?.name.toString() }
            val permissions = query.stream().map { it.get(rolePermissionEntity.id.permissionName) }.collect(Collectors.toSet())
                .joinToString(separator = ", ") { it?.name.toString() }
            return MemberAttributes(member!!.id!!, member.oauthType, member.oauthId, member.nickname, member.profileImageUrl, roles, permissions)
        }

        return null
    }
}