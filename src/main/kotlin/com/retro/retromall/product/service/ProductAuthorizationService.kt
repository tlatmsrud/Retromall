package com.retro.retromall.product.service

import com.retro.exception.RetromallException
import com.retro.retromall.authorization.enums.Permission
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.ProductEntity
import org.springframework.stereotype.Service

@Service
class ProductAuthorizationService {
    fun checkPermission(targetProduct: ProductEntity, authenticationAttributes: AuthenticationAttributes, type: Permission) {
        if (targetProduct.authorId == authenticationAttributes.id!!) {
            validPermission(authenticationAttributes.permissions!!, type)
        } else {
            throw RetromallException("${type.getMessage()} 권한이 없습니다.")
        }
    }

    private fun validPermission(permissions: String?, type: Permission) {
        permissions?.run {
            val permissionList = split(", ")
            if (type.name in permissionList) {
                return
            }
        }
        throw RetromallException("${type.getMessage()} 권한이 없습니다.")
    }
}