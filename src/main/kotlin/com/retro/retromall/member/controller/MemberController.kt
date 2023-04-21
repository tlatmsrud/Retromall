package com.retro.retromall.member.controller

import com.retro.retromall.member.infra.client.dto.kakao.KakaoCodeDto
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.client.dto.naver.NaverCodeDto
import com.retro.retromall.member.service.MemberReadService
import com.retro.retromall.member.service.MemberService
import com.retro.util.HttpUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.LOCATION
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
    private val memberReadService: MemberReadService,

    @Value("\${refresh-token-cookie.name}")
    private val refreshTokenCookieName: String,

    @Value("\${refresh-token-cookie.path}")
    private val refreshTokenCookiePath: String,

    @Value("\${refresh-token-cookie.day}")
    private val refreshTokenCookieDay: Long
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MemberController::class.java)
    }

    @GetMapping("/oauth/kakao")
    fun login(@ModelAttribute kakaoCodeDto: KakaoCodeDto): ResponseEntity<LoginResponse.Attributes> {
        val result = memberReadService.findMemberByOAuth(OAuthType.KAKAO, kakaoCodeDto)

        val headers = HttpHeaders()
        headers.add(SET_COOKIE, getRefreshCookie(result.refreshToken).toString())
        headers.add(LOCATION, "http://localhost:3000/auth/kakao"+"?accessToken="+result.attributes.tokenAttributes.accessToken)
        return ResponseEntity
            .status(HttpStatus.MOVED_PERMANENTLY)
            .headers(headers).build()
    }

    @GetMapping("/oauth/naver")
    fun login(@ModelAttribute naverCodeDto: NaverCodeDto): ResponseEntity<LoginResponse.Attributes> {
        val result = memberReadService.findMemberByOAuth(OAuthType.NAVER, naverCodeDto)

        val headers = HttpHeaders()
        headers.add(SET_COOKIE, getRefreshCookie(result.refreshToken).toString())
        headers.add(LOCATION, "http://localhost:3000/auth/naver"+"?accessToken="+result.attributes.tokenAttributes.accessToken)
        return ResponseEntity
            .status(HttpStatus.MOVED_PERMANENTLY)
            .headers(headers).build()

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