package com.retro.retromall.token.domain

import com.retro.retromall.member.domain.Member
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="tb_token")
class Token (
    member : Member,
    refreshToken : String
){

    @Id
    @Column(name = "token_id")
    val id : Long? = null

    @OneToOne
    @JoinColumn(name="member_id")
    var member : Member = member

    @Column(name = "refresh_token")
    var refreshToken :String = refreshToken

    fun updateRefreshToken(refreshToken: String){
        this.refreshToken = refreshToken
    }

}