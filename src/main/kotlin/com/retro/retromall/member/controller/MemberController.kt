package com.retro.retromall.member.controller

import com.retro.retromall.member.dto.TokenAttributes
import com.retro.retromall.member.dto.LoginAttributes
import com.retro.retromall.member.service.MemberWriteService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberWriteService: MemberWriteService
) {
    @PostMapping("/login")
    fun login(@RequestBody loginAttributes: LoginAttributes): ResponseEntity<TokenAttributes> {
        val tokenInfo = memberWriteService.findMemberByOauth(loginAttributes.oAuthType, loginAttributes.authorizationCode)
        return ResponseEntity.ok(tokenInfo)
    }
}