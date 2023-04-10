package com.retro.retromall.token

import com.retro.retromall.token.dto.TokenAttributes
import com.retro.retromall.token.service.TokenService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/token")
class TokenController(
    private val tokenService: TokenService
) {
    private val logger: Logger = LoggerFactory.getLogger(TokenController::class.java)

    @PatchMapping
    fun tokenRenew( @CookieValue("refresh_token", required = true) refreshToken : String)
    : ResponseEntity<TokenAttributes> {

        var tokenAttributes = tokenService.renewAccessToken(refreshToken)

        return ResponseEntity.ok()
            .header(SET_COOKIE, tokenAttributes.generateRefreshTokenCookie().toString())
            .body(tokenAttributes)

    }
}