package com.retro.exception

class UnauthorizedAccessException(override val message: String?) : RuntimeException(message) {
}