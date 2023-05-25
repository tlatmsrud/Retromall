package com.retro.retromall.member.controller

import com.retro.retromall.auth.client.dto.OAuthAuthorizationCode
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.service.MemberService
import com.retro.util.HttpUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
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
    @GetMapping("/oauth/login/kakao")
    fun loginKakao(@ModelAttribute oAuthAuthorizationCode: OAuthAuthorizationCode): ResponseEntity<LoginResponse.Attributes> {
        val result = memberService.loginMemberWithOAuth(OAuthType.KAKAO, oAuthAuthorizationCode)

        val headers = HttpHeaders()
        headers.add(SET_COOKIE, getRefreshCookie(result.refreshToken).toString())
        return ResponseEntity
            .status(HttpStatus.OK)
            .headers(headers)
            .body(result.attributes)
    }

    @GetMapping("/oauth/login/naver")
    fun loginNaver(@ModelAttribute oAuthAuthorizationCode: OAuthAuthorizationCode): ResponseEntity<LoginResponse.Attributes> {
        val result = memberService.loginMemberWithOAuth(OAuthType.NAVER, oAuthAuthorizationCode)

        val headers = HttpHeaders()
        headers.add(SET_COOKIE, getRefreshCookie(result.refreshToken).toString())
        return ResponseEntity
            .status(HttpStatus.OK)
            .headers(headers)
            .body(result.attributes)
    }

    private fun getRefreshCookie(refreshToken: String): ResponseCookie {
        return HttpUtils.generateCookie(
            refreshTokenCookieName,
            refreshToken,
            refreshTokenCookiePath,
            refreshTokenCookieDay
        )
    }
}