package com.retro.retromall.member.controller

import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.dto.LoginRequest
import com.retro.retromall.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val loginResponse = memberService.findMemberByOauth(loginRequest.oAuthType, loginRequest.authorizationCode)
        return ResponseEntity.ok(loginResponse)
    }
}