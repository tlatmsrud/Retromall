package com.retro.retromall.token.domain.repository

import com.retro.retromall.token.domain.Token
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
interface TokenRepository :JpaRepository<Token,Long> {

    fun findByRefreshToken(refreshToken: String) : Optional<Token>
}