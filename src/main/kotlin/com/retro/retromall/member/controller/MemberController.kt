package com.retro.retromall.member.controller

import com.retro.retromall.member.dto.TokenInfo
import com.retro.retromall.member.dto.LoginRequest
import com.retro.retromall.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<TokenInfo> {
        val tokenInfo = memberService.findMemberByOauth(loginRequest.oAuth2Type, loginRequest.authorizationCode)
        return ResponseEntity.ok(tokenInfo)
    }
}