package com.retro.retromall.util

import org.springframework.security.core.context.SecurityContextHolder

class SecurityUtils private constructor() {
    companion object {
        fun getCurrentMember(): String {
            val authentication = SecurityContextHolder.getContext().authentication
            authentication
                ?.let { return authentication.name }
                ?: throw RuntimeException("인증 정보가 없습니다.")
        }
    }
}