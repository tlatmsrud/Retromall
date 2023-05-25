package com.retro.retromall.authorization.domain

import com.retro.retromall.authorization.enums.PermissionName
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
    val roleName: Role,

    @Column(name = "permission_name", nullable = false)
    @Enumerated(EnumType.STRING)
    val permissionName: PermissionName
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RolePermissionEntityId

        if (roleName != other.roleName) return false
        return permissionName == other.permissionName
    }

    override fun hashCode(): Int {
        var result = roleName.hashCode()
        result = 31 * result + permissionName.hashCode()
        return result
    }
}