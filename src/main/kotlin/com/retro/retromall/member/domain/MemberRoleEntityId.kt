package com.retro.retromall.member.domain

import com.retro.retromall.authorization.enums.Role
import java.io.Serializable
import javax.persistence.*

@Embeddable
class MemberRoleEntityId(
    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    val roleName: Role,

    @Column(name = "member_id", nullable = false)
    val memberId: Long
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberRoleEntityId

        if (roleName != other.roleName) return false
        return memberId == other.memberId
    }

    override fun hashCode(): Int {
        var result = roleName.hashCode()
        result = 31 * result + memberId.hashCode()
        return result
    }
}