package com.retro.retromall.token

import com.retro.retromall.token.dto.TokenAttributes
import com.retro.retromall.token.service.TokenService
import com.retro.util.HttpUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/token")
class TokenController(
    private val tokenService: TokenService,

    @Value("\${refresh-token-cookie.name}")
    private val refreshTokenCookieName: String,

    @Value("\${refresh-token-cookie.path}")
    private val refreshTokenCookiePath: String,

    @Value("\${refresh-token-cookie.day}")
    private val refreshTokenCookieDay: Long
) {
    private val logger: Logger = LoggerFactory.getLogger(TokenController::class.java)

    @PatchMapping
    fun tokenRenew( @CookieValue("refresh_token", required = true) refreshToken : String)
    : ResponseEntity<TokenAttributes> {

        val tokenAttributes = tokenService.renewAccessToken(refreshToken)

        val tokenCookie = HttpUtils.generateCookie(
            refreshTokenCookieName,
            tokenAttributes.refreshToken,
            refreshTokenCookiePath,
            refreshTokenCookieDay
        )
        return ResponseEntity.ok()
            .header(SET_COOKIE,tokenCookie.toString())
            .body(tokenAttributes)

    }
}