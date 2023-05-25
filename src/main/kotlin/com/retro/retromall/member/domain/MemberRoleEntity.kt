package com.retro.retromall.member.domain

import com.retro.retromall.authorization.domain.RoleEntity
import javax.persistence.*

@Entity
@Table(name = "tb_member_role")
class MemberRoleEntity(
    @EmbeddedId
    val id: MemberRoleEntityId,

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val memberEntity: MemberEntity
) {
    constructor(roleEntity: RoleEntity, memberEntity: MemberEntity) : this(MemberRoleEntityId(roleEntity.name, memberEntity.id!!), memberEntity)
}