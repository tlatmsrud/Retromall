package com.retro.retromall.token.domain


import javax.persistence.*

/**
 * Domain For Token
 * @author sim
 */
@Entity
@Table(name="tb_token")
class Token (
    memberId : Long,
    refreshToken : String,
    expirationRefreshToken: Long
) {


    @Id
    var memberId : Long = memberId

    @Column(name = "refresh_token")
    var refreshToken :String = refreshToken

    @Column(name = "expiration_refresh_token")
    var expirationRefreshToken: Long = expirationRefreshToken

    /**
     * 리프레시 토큰 수정
     * @author sim
     * @param refreshToken - 리프레시 토큰
     */
    fun updateRefreshToken(refreshToken: String){
        this.refreshToken = refreshToken
    }


}