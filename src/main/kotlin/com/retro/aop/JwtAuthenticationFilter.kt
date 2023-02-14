package com.retro.aop

import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

//@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        if ("/members/login" == httpServletRequest.requestURI) {
            chain?.doFilter(request, response)
            return
        }

        val token = resolveToken(httpServletRequest)
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            chain?.doFilter(request, response)
        }
    }

    private fun resolveToken(request: HttpServletRequest): String {
        val token = request.getHeader("Authorization")
        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            return token.substring(7)
        }

        return "";
    }
}