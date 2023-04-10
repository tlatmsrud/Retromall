package com.retro.retromall.member.controller

import com.retro.retromall.member.dto.LoginRequest
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.service.MemberService
import com.retro.retromall.token.service.TokenService
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
    private val tokenService: TokenService
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val loginResponse = memberService.findMemberByOauth(loginRequest)
        val refreshToken = loginResponse.tokenAttributes.refreshToken
        return ResponseEntity.ok()
            .header(SET_COOKIE,
                tokenService.generateRefreshTokenCookie(refreshToken).toString())
            .body(loginResponse)

    }

}