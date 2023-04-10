package com.retro.retromall.token

import com.retro.retromall.token.dto.TokenAttributes
import com.retro.retromall.token.service.TokenService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller For Token
 * @author sim
 */
@RestController
@RequestMapping("/api/token")
class TokenController(
    private val tokenService: TokenService
) {
    private val logger: Logger = LoggerFactory.getLogger(TokenController::class.java)

    /**
     * 액세스 토큰을 갱신한다.
     * 리프레시 토큰에 매핑된 계정에 대한 액세스 토큰을 갱신한다.
     * 갱신 후 응답 객체 리턴 시 리프레시 토큰에 대한 쿠키를 세팅한다.
     * @author sim
     *
     * @param refreshToken -  리프레시 토큰 (By Cookie : refresh_token)
     * @return 토큰에 대한 ResponseEntity.
     */
    @PatchMapping
    fun tokenRenew( @CookieValue("refresh_token", required = true) refreshToken : String)
    : ResponseEntity<TokenAttributes> {

        var tokenAttributes = tokenService.renewAccessToken(refreshToken)

        return ResponseEntity.ok()
            .header(SET_COOKIE, tokenAttributes.generateRefreshTokenCookie().toString())
            .body(tokenAttributes)

    }
}