package com.retro.retromall.member.support

import com.retro.retromall.member.dto.TokenInfo
import com.retro.retromall.member.domain.Member
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val secretKey: String
) {
    private val logger: Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    private val keyBytes: ByteArray? = Decoders.BASE64.decode(secretKey)
    private val key = Keys.hmacShaKeyFor(keyBytes)

    fun generateToken(member: Member): TokenInfo {
        val authorities = member.getAuthorities().stream()
            .map { role -> role.name }
            .collect(Collectors.joining(","))

        val now = Date().time
        val accessTokenExpiresIn = Date(now + 8640000)
        val accessToken = Jwts.builder()
            .setSubject(member.getUsername())
            .claim("auth", authorities)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        val refreshToken = Jwts.builder()
            .setExpiration(Date(now + 8640000))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return TokenInfo(grantType = "Bearer", accessToken = accessToken, refreshToken = refreshToken)
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
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}