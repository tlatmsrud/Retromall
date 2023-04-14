package com.retro.retromall.member.domain

import com.retro.retromall.permission.domain.Permission
import javax.persistence.*

@Entity
@Table(name = "tb_role_permission")
class RolePermission(
    @EmbeddedId
    val id: RolePermissionKey,

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    val role: Role,

    @MapsId("permissionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    val permission: Permission
) {
    constructor(role: Role, permission: Permission) : this(
        RolePermissionKey(role.id!!, permission.id!!),
        role,
        permission
    )
}