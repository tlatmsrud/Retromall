package com.retro.retromall.member.controller

import com.retro.retromall.member.dto.LoginRequest
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.service.MemberService
import com.retro.util.HttpUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService,

    @Value("\${refresh-token-cookie.name}")
    private val refreshTokenCookieName: String,

    @Value("\${refresh-token-cookie.path}")
    private val refreshTokenCookiePath: String,

    @Value("\${refresh-token-cookie.day}")
    private val refreshTokenCookieDay: Long
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {

        val loginResponse = memberService.findMemberByOauth(loginRequest)
        val tokenCookie = HttpUtils.generateCookie(
            refreshTokenCookieName,
            loginResponse.tokenAttributes.refreshToken,
            refreshTokenCookiePath,
            refreshTokenCookieDay
        )
        return ResponseEntity.ok()
            .header(SET_COOKIE,tokenCookie.toString())
            .body(loginResponse)
    }
}