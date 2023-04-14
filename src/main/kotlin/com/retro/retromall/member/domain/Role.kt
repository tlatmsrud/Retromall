package com.retro.retromall.member.domain

import com.retro.retromall.member.enums.Role
import com.retro.retromall.permission.domain.Permission
import javax.persistence.*

@Entity
@Table(name = "tb_role")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
    @Enumerated
    val name: Role,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_role_permission",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    val permissions: Set<Permission> = mutableSetOf()
)