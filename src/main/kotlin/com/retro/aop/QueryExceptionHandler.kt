package com.retro.aop

import org.hibernate.QueryException
import org.springframework.web.bind.annotation.ExceptionHandler
import java.sql.SQLIntegrityConstraintViolationException

//@ControllerAdvice
class QueryExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException::class)
    fun sqlIntegrityConstraintViolationException() {
        return
    }

    @ExceptionHandler(QueryException::class)
    fun queryException() {
        return
    }
}