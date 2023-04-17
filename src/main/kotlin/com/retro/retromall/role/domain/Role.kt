package com.retro.retromall.role.domain

import com.retro.retromall.role.enums.Role
import javax.persistence.*

@Entity
@Table(name = "tb_role")
class Role(
    @Id
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    val name: Role,

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    val permissions: MutableSet<RolePermission>
)