package com.retro.exception

import org.springframework.http.HttpStatus

open class RetromallException(
    override val message: String,
    open val httpStatus: HttpStatus
) : RuntimeException(message)