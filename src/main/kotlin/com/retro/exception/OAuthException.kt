package com.retro.exception

class OAuthException(
    override val message: String
) : RuntimeException(message) {
}