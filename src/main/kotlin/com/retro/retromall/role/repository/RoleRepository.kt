package com.retro.retromall.role.repository

import com.retro.retromall.role.domain.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, com.retro.retromall.role.enums.Role> {
    fun findByName(name: com.retro.retromall.role.enums.Role): Role?
}