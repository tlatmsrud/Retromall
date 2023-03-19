package com.retro.retromall.product.security

import com.retro.retromall.product.domain.Product
import com.retro.security.AuthenticationService
import org.springframework.stereotype.Component

@Component
class ProductAuthenticationServiceImpl : AuthenticationService {
    override fun validateUser(user: Any, target: Any): Boolean {
        val product = target as Product

        return product.isAuthor(user as Long)
    }
}