package com.retro.retromall.role.domain

import javax.persistence.*

@Entity
@Table(name = "tb_role_permission")
class RolePermission(
    @EmbeddedId
    val id: RolePermissionKey,

    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name")
    val role: Role,
)