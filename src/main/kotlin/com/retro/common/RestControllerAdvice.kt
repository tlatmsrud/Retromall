package com.retro.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestControllerAdvice {
    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateException(e: Exception) : ResponseEntity<Any> {
        return ResponseEntity.ok(ErrorResponse(e.message))
    }

    data class ErrorResponse(
        val message: String?,
    )
}