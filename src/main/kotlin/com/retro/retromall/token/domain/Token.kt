package com.retro.retromall.token.domain

import com.retro.retromall.member.domain.Member
import javax.persistence.*

/**
 * Domain For Token
 * @author sim
 */
@Entity
@Table(name="tb_token")
class Token (
    member : Member,
    refreshToken : String,
    expirationRefreshToken: Long
){

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    var id : Long? = null

    @OneToOne
    @JoinColumn(name="member_id")
    var member : Member = member

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