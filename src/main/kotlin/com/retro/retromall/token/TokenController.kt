package com.retro.retromall.token

import com.retro.retromall.token.dto.TokenResponse
import com.retro.retromall.token.service.TokenService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/token")
class TokenController(
    private val tokenService: TokenService
) {
    private val logger: Logger = LoggerFactory.getLogger(TokenController::class.java)

    @PostMapping("/renew")
    fun tokenRenew( @CookieValue("refresh_token", required = true) refreshToken : String, httpResponse : HttpServletResponse): ResponseEntity<TokenResponse> {

        var tokenResponse = tokenService.renewAccessToken(refreshToken)

        var cookie = Cookie("refresh_token", tokenResponse.tokenAttributes.refreshToken)
        cookie.secure = true
        cookie.isHttpOnly = true
        cookie.path = "/api/token/renew"
        httpResponse.addCookie(cookie)

        return ResponseEntity.ok(tokenResponse)
    }

}