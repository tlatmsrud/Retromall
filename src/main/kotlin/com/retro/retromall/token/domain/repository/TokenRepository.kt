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
     * 리프레시 토큰에 대한 토큰 엔티티를 조회한다.
     * @author sim
     *
     * @param refreshToken - 리프레시 토큰
     * @return 토큰 객체
     */
    fun findByRefreshToken(refreshToken: String) : Optional<Token>

    /**
     * ID에 대한 토큰 엔티티를 조회한다.
     * @author sim
     *
     * @param memberId - 유저 ID
     * @return 토큰 객체
     */
    fun findByMemberId(memberId : Long) : Token?
}