package com.retro.retromall.authorization.repository

import com.retro.retromall.authorization.domain.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<RoleEntity, com.retro.retromall.authorization.enums.Role> {
    fun findByName(name: com.retro.retromall.authorization.enums.Role): RoleEntity?
}