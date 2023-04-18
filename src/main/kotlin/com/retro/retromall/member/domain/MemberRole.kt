package com.retro.retromall.member.domain

import com.retro.retromall.role.domain.Role
import javax.persistence.*

@Entity
@Table(name = "tb_member_role")
class MemberRole(
    @EmbeddedId
    val id: MemberRoleKey,

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member
) {
    constructor(role: Role, member: Member) : this(MemberRoleKey(role.name, member.id!!), member)
}