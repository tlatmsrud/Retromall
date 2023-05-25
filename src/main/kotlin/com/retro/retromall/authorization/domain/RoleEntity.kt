package com.retro.retromall.authorization.domain

import com.retro.retromall.authorization.enums.Role
import javax.persistence.*

@Entity
@Table(name = "tb_role")
class RoleEntity(
    @Id
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    val name: Role,

    @OneToMany(mappedBy = "roleEntity", fetch = FetchType.LAZY)
    val permissions: MutableSet<RolePermissionEntity>
)