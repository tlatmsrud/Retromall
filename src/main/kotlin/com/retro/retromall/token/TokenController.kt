package com.retro.retromall.token

import com.retro.retromall.token.dto.TokenDto
import com.retro.retromall.token.service.TokenService
import com.retro.util.HttpUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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
    private val tokenService: TokenService,

    @Value("\${refresh-token-cookie.name}")
    private val refreshTokenCookieName: String,

    @Value("\${refresh-token-cookie.path}")
    private val refreshTokenCookiePath: String,

    @Value("\${refresh-token-cookie.day}")
    private val refreshTokenCookieDay: Long
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
    : ResponseEntity<TokenDto> {

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