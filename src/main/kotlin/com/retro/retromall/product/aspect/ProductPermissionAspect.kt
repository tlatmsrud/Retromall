package com.retro.retromall.product.aspect

import com.retro.exception.RetromallException
import com.retro.retromall.member.dto.AuthenticationAttributes
import com.retro.retromall.product.domain.repository.ProductRepository
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class ProductPermissionAspect(
    private val productRepository: ProductRepository,
) {
    @Around("@annotation(CheckUserPermission) && args(authenticationAttributes, productId, ..)")
    fun checkPermission(proceedingJoinPoint: ProceedingJoinPoint, authenticationAttributes: AuthenticationAttributes, productId: Long): Any {
        val productEntity = productRepository.findById(productId).orElseThrow()
        if (productEntity.isAuthor(authenticationAttributes.id!!)) {
            proceedingJoinPoint.proceed()
        }
        throw RetromallException("해당 상품에 대한 권한이 없습니다.")
    }
}