package com.retro.retromall.product.security

import com.retro.retromall.product.domain.ProductEntity
import com.retro.security.AuthenticationService
import org.springframework.stereotype.Component

@Component
class ProductAuthenticationServiceImpl : AuthenticationService {
    override fun validateUser(user: Any, target: Any): Boolean {
        val productEntity = target as ProductEntity

        return productEntity.isAuthor(user as Long)
    }
}