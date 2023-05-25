package com.retro.retromall.authorization.domain

import javax.persistence.*

@Entity
@Table(name = "tb_role_permission")
class RolePermissionEntity(
    @EmbeddedId
    val id: RolePermissionEntityId,

    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name")
    val roleEntity: RoleEntity,
)