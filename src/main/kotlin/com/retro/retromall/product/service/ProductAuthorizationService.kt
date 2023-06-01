package com.retro.retromall.product.service

import com.retro.exception.UnauthorizedAccessException
import com.retro.retromall.authorization.enums.Permission
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.ProductEntity
import org.springframework.stereotype.Service

@Service
class ProductAuthorizationService {
    fun checkPermission(
        targetProduct: ProductEntity,
        authenticationAttributes: AuthenticationAttributes,
        type: Permission
    ) {
        val userId = authenticationAttributes.id
        val permissions = authenticationAttributes.permissions

        if (userId == null || targetProduct.authorId != userId || permissions == null) {
            throw UnauthorizedAccessException("${type.getMessage()} 권한이 없습니다.")
        }

        validatePermission(permissions, type)
    }

    private fun validatePermission(permissions: String, type: Permission) {
        val permissionList = permissions.split(", ").mapNotNull { Permission.fromValue(it) }
        if (type !in permissionList) {
            throw UnauthorizedAccessException("${type.getMessage()} 권한이 없습니다.")
        }
    }
}