package com.retro.exception

class ProductException(
    override val message: String
) : RuntimeException(message)