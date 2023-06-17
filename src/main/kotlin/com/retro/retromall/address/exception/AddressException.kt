package com.retro.retromall.address.exception

import com.retro.exception.RetromallException
import org.springframework.http.HttpStatus

class AddressException(
    override val message: String,
    override val httpStatus: HttpStatus
) : RetromallException(message, httpStatus)