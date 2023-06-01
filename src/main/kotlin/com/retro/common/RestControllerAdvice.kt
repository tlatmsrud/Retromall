package com.retro.common

import com.retro.exception.UnauthorizedAccessException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SecurityException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException


@ControllerAdvice
class RestControllerAdvice {
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(e : MethodArgumentNotValidException) : ResponseEntity<Any>{
        val message = e.bindingResult.allErrors.stream().findFirst()
            .get().defaultMessage
        return ResponseEntity.ok(ErrorResponse(message))
    }
    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(e: ConstraintViolationException): ResponseEntity<Any?>? {
        val message = e.constraintViolations.stream().findFirst()
            .get().message
        return ResponseEntity.ok(ErrorResponse(message))
    }

    @ExceptionHandler(UnauthorizedAccessException::class)
    fun unauthorizedException(e : UnauthorizedAccessException): ResponseEntity<Any> {
        val message = e.message
        return ResponseEntity(message, HttpStatus.UNAUTHORIZED)
    }

    data class ErrorResponse(
        val message: String?,
    )
}