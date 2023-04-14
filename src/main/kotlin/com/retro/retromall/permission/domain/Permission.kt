package com.retro.retromall.permission.domain

import com.retro.retromall.permission.enums.Permission
import javax.persistence.*

@Entity
@Table(name = "tb_permission")
class Permission(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", length = 20, nullable = false, unique = true)
    @Enumerated
    val name: Permission
)