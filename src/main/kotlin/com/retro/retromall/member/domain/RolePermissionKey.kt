package com.retro.retromall.member.domain

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class RolePermissionKey(
    @Column(name = "role_id", nullable = false)
    val roleId: Long,

    @Column(name = "permission_id", nullable = false)
    val permissionId: Long
) : Serializable