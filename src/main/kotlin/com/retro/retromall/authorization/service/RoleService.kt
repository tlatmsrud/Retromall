package com.retro.retromall.authorization.service

import com.retro.retromall.authorization.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {


}