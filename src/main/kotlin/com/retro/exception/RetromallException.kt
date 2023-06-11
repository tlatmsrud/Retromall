package com.retro.exception

class RetromallException(
    override val message: String
) : RuntimeException(message)