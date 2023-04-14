package com.retro.retromall.member.domain

import java.io.Serializable
import javax.persistence.*

@Embeddable
class MemberRoleKey(
    @Column(name = "role_id", nullable = false)
    val roleId: Long,

    @Column(name = "member_id", nullable = false)
    val memberId: Long
) : Serializable