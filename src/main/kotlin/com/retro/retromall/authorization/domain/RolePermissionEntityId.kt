package com.retro.retromall.authorization.domain

import com.retro.retromall.authorization.enums.Permission
import com.retro.retromall.authorization.enums.Role
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class RolePermissionEntityId(
    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: Role,

    @Column(name = "permission_name", nullable = false)
    @Enumerated(EnumType.STRING)
    val permission: Permission
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RolePermissionEntityId

        if (role != other.role) return false
        return permission == other.permission
    }

    override fun hashCode(): Int {
        var result = role.hashCode()
        result = 31 * result + permission.hashCode()
        return result
    }
}