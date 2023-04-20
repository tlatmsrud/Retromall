package com.retro.common

import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.token.dto.TokenDto
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val secretKey: String
) {
    private val logger: Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    private val keyBytes: ByteArray? = Decoders.BASE64.decode(secretKey)
    private val key = Keys.hmacShaKeyFor(keyBytes)

    fun generateToken(attributes: MemberAttributes): TokenDto {
        val now = Date()
        val accessTokenExpiresIn = Date(now.time + 86400000)
        val refreshTokenExpiresIn = Date(now.time + 2592000000)
        val accessToken = Jwts.builder()
            .setIssuer("Retromall")
            .setSubject("Retromall Jwt Token")
            .setAudience("Retroall User")
            .setIssuedAt(now)
            .claim("id", attributes.id)
            .claim("nickName", attributes.nickName)
            .claim("roles", attributes.roles)
            .claim("permissions", attributes.permissions)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        val refreshToken = Jwts.builder()
            .setExpiration(refreshTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return TokenDto(
            grantType = "Bearer",
            accessToken = accessToken,
            refreshToken = refreshToken,
            expirationAccessToken = accessTokenExpiresIn.time,
            expirationRefreshToken = refreshTokenExpiresIn.time
        )
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            logger.info("Invalid Jwt Token", e)
        } catch (e: MalformedJwtException) {
            logger.info("Invalid Jwt Token", e)
        } catch (e: ExpiredJwtException) {
            logger.info("Expired Jwt Token", e)
        } catch (e: UnsupportedJwtException) {
            logger.info("Unsupported Jwt Token", e)
        } catch (e: IllegalArgumentException) {
            logger.info("Jwt Claims String is empty", e)
        }
        return false
    }

    fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().deserializeJsonWith(JacksonDeserializer()).setSigningKey(key).build()
                .parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}