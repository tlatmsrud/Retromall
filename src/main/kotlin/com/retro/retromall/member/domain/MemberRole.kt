package com.retro.retromall.member.domain

import javax.persistence.*

@Entity
@Table(name = "tb_member_role")
class MemberRole(
    @EmbeddedId
    val id: MemberRoleKey,

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    val role: Role,

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member
) {
    constructor(role: Role, member: Member) : this(MemberRoleKey(role.id!!, member.id!!), role, member)
}