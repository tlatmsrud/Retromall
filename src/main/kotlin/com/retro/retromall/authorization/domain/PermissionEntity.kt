package com.retro.retromall.authorization.domain

import com.retro.retromall.authorization.enums.PermissionName
import javax.persistence.*

@Entity
@Table(name = "tb_permission")
class PermissionEntity(
    @Id
    @Column(name = "name", length = 20, nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    val name: PermissionName
)