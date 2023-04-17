package com.retro.retromall.role.domain

import com.retro.retromall.role.enums.PermissionName
import javax.persistence.*

@Entity
@Table(name = "tb_permission")
class Permission(
    @Id
    @Column(name = "name", length = 20, nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    val name: PermissionName
)