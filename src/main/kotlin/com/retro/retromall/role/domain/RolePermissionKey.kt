package com.retro.retromall.role.domain

import com.retro.retromall.role.enums.PermissionName
import com.retro.retromall.role.enums.Role
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class RolePermissionKey(
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

        other as RolePermissionKey

        if (roleName != other.roleName) return false
        return permissionName == other.permissionName
    }

    override fun hashCode(): Int {
        var result = roleName.hashCode()
        result = 31 * result + permissionName.hashCode()
        return result
    }
}