package com.retro.retromall.token

import com.retro.retromall.token.dto.TokenResponse
import com.retro.retromall.token.service.TokenService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/token")
class TokenController(
    private val tokenService: TokenService
) {
    private val logger: Logger = LoggerFactory.getLogger(TokenController::class.java)

    @PostMapping("/renew")
    fun tokenRenew(
        @RequestHeader("REFRESH_TOKEN") refreshToken : String): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(tokenService.renewAccessToken(refreshToken))
    }

}