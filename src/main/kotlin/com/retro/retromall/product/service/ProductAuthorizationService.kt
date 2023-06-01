package com.retro.retromall.product.service

import com.retro.exception.RetromallException
import com.retro.retromall.authorization.enums.Permission
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.ProductEntity
import org.springframework.stereotype.Service

@Service
class ProductAuthorizationService {
    fun checkReadPermission(product: ProductEntity, authenticationAttributes: AuthenticationAttributes) {

    }

    fun checkUpdatePermission(product: ProductEntity, authenticationAttributes: AuthenticationAttributes) {

    }

    fun checkDeletePermission(product: ProductEntity, authenticationAttributes: AuthenticationAttributes) {

    }

    private fun validPermission(userPermissions: String, type: Permission) {
        if (!userPermissions.contains(type.name))
            throw RetromallException(type.getKorName() + " 권한이 없습니다.")
    }
}