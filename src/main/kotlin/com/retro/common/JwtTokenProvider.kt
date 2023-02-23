package com.retro.common

import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.dto.TokenAttributes
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

    fun generateToken(member: Member): TokenAttributes {
        val now = Date().time
        val accessTokenExpiresIn = Date(now + 8640000)
        val accessToken = Jwts.builder()
            .setSubject(member.getUsername())
            .claim("id", member.id!!)
            .claim("nickName", member.nickname)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        val refreshToken = Jwts.builder()
            .setExpiration(Date(now + 8640000))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return TokenAttributes(grantType = "Bearer", accessToken = accessToken, refreshToken = refreshToken)
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
            Jwts.parserBuilder().deserializeJsonWith(JacksonDeserializer()).setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}