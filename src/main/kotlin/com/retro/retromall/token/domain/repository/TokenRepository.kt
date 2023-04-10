package com.retro.retromall.token.domain.repository

import com.retro.retromall.token.domain.Token
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Repository For Token
 * @author sim
 */
interface TokenRepository :JpaRepository<Token,Long> {

    /**
     * 리프레시 토큰을 조회한다.
     * @author sim
     * 
     * @param refreshToken - 리프레시 토큰
     * @return 토큰에 대한 Optional 객체
     */
    fun findByRefreshToken(refreshToken: String) : Optional<Token>
}