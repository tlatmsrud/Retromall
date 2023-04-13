package com.retro.common

import com.retro.common.annotation.MemberAuthentication
import com.retro.retromall.member.dto.MemberAttributes
import io.jsonwebtoken.Claims
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

@Component
class MemberAttributeResolver(
    private val jwtTokenProvider: JwtTokenProvider
) : HandlerMethodArgumentResolver {
    private val logger: Logger = LoggerFactory.getLogger(MemberAttributeResolver::class.java)
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == MemberAttributes::class.java &&
                parameter.hasParameterAnnotation(MemberAuthentication::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val authorization = (webRequest.nativeRequest as HttpServletRequest).getHeader("Authorization")
        if (!parameter.getParameterAnnotation(MemberAuthentication::class.java)!!.required && !StringUtils.hasText(
                authorization
            )
        ) {
            return MemberAttributes(id = null, role = "ANONYMOUS", permissions = null)
        }

        //Test Token
        if (authorization == "Bearer TestToken") {
            return MemberAttributes(
                id = 1000L,
                role = "USER",
                permissions = setOf("CREATE_PRODUCT", "MODIFY_PRODUCT", "DELETE_PRODUCT")
            )
        }

        val token = resolveToken(authorization)
        jwtTokenProvider.validateToken(token)
        val claims = jwtTokenProvider.parseClaims(token)
        return createMemberAttributes(claims)
    }

    private fun createMemberAttributes(claims: Claims): MemberAttributes {
        val id: Long? = claims.get("id", Integer::class.java)?.toLong()
        val role = claims.get("role", String::class.java)
        val permissions = claims.get("permissions", String::class.java)
        return MemberAttributes(id = id, role = role, permissions = resolvePermission(permissions))
    }

    private fun resolvePermission(permissions: String): Set<String> {
        return permissions.split(", ").toSet()
    }

    private fun resolveToken(authorization: String): String {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
            return authorization.substring(7)
        }

        return ""
    }
}