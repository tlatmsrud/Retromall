package com.retro.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestControllerAdvice {
    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateException(e: IllegalStateException) : ResponseEntity<Any> {
        return ResponseEntity.ok(ErrorResponse(e.message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(e: IllegalArgumentException): ResponseEntity<Any> {
        return ResponseEntity.ok(ErrorResponse(e.message))
    }

    data class ErrorResponse(
        val message: String?,
    )
}