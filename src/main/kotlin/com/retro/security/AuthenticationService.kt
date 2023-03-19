package com.retro.security

interface AuthenticationService {
    fun validateUser(user: Any, target: Any): Boolean
}