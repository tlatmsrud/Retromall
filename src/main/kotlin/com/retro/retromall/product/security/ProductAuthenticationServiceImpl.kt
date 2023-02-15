package com.retro.retromall.product.security

import com.retro.retromall.member.domain.Member
import com.retro.retromall.product.domain.Product
import com.retro.security.AuthenticationService
import org.springframework.stereotype.Component

@Component
class ProductAuthenticationServiceImpl : AuthenticationService {
    override fun validateUser(user: Any, target: Any) {
        val member = user as Member
        val product = target as Product

        return product.isAuthor(member)
    }
}