package com.retro.exception

class ProductNotFoundException(
    override val message: String
) : RuntimeException(message)