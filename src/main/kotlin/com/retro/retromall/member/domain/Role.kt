package com.retro.retromall.member.domain

import javax.persistence.*

@Entity
@Table(name = "tb_role")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_role_permission",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    val permissions: Set<Permission> = mutableSetOf()
)