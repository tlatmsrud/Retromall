package com.retro.common

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SecurityException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestControllerAdvice {
    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateException(e: IllegalStateException): ResponseEntity<Any> {
        return ResponseEntity.ok(ErrorResponse(e.message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(e: IllegalArgumentException): ResponseEntity<Any> {
        return ResponseEntity.ok(ErrorResponse(e.message))
    }

    @ExceptionHandler(SecurityException::class)
    fun securityException(e: SecurityException): ResponseEntity<Any> {
        return ResponseEntity.ok(ErrorResponse(e.message))
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun malformedJwtException(e: MalformedJwtException): ResponseEntity<Any> {
        return ResponseEntity.ok(ErrorResponse(e.message))
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun expiredJwtException(e: ExpiredJwtException): ResponseEntity<Any> {
        return ResponseEntity.ok(ErrorResponse(e.message))
    }

    @ExceptionHandler(UnsupportedJwtException::class)
    fun unsupportedJwtException(e: UnsupportedJwtException): ResponseEntity<Any> {
        return ResponseEntity.ok(ErrorResponse(e.message))
    }

    data class ErrorResponse(
        val message: String?,
    )
}