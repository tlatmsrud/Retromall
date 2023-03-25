package com.retro.common

import com.retro.common.annotation.MemberAuthentication
import com.retro.retromall.member.dto.MemberAttributes
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
        if (!parameter.getParameterAnnotation(MemberAuthentication::class.java)!!.required && !StringUtils.hasText(authorization)) {
            return MemberAttributes(id = null)
        }

        //Test Token
        if (authorization == "Bearer TestToken") {
            return MemberAttributes(id = 1000L)
        }

        val token = resolveToken(authorization)
        jwtTokenProvider.validateToken(token)
        val claims = jwtTokenProvider.parseClaims(token)
        val id: Long? = claims.get("id", Integer::class.java)?.toLong()
        return MemberAttributes(id = id)
    }

    private fun resolveToken(authorization: String): String {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
            return authorization.substring(7)
        }

        return ""
    }
}