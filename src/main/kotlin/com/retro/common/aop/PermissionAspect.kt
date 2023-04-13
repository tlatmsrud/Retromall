package com.retro.common.aop

import com.retro.retromall.member.dto.MemberAttributes
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class PermissionAspect {
    @Around("@annotation(requirePermission)")
    fun checkPermission(joinPoint: ProceedingJoinPoint, requirePermission: RequirePermission) {
        val args = joinPoint.args
        val memberAttributes = args.firstOrNull { it is MemberAttributes } as? MemberAttributes
            ?: throw IllegalStateException("MemberAttributes not found in ${joinPoint.signature.name}")
        val hasPermission =
            memberAttributes.permissions?.contains(requirePermission.value)
                ?: throw IllegalStateException("Permissions property not initialized")

        if (!hasPermission)
            // 각 권한별 코드로 사용자에게 반환
            throw IllegalStateException("")
    }
}