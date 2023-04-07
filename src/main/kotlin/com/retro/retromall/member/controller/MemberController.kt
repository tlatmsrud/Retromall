package com.retro.retromall.member.controller

import com.retro.retromall.member.dto.LoginRequest
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest, httpResponse : HttpServletResponse): ResponseEntity<LoginResponse> {
        val loginResponse = memberService.findMemberByOauth(loginRequest)

        val cookie = Cookie("refresh_token", loginResponse.tokenAttributes.refreshToken)
        cookie.secure = true
        cookie.isHttpOnly = true
        cookie.path = "/api/token/renew"
        httpResponse.addCookie(cookie)

        return ResponseEntity.ok(loginResponse)

    }

}