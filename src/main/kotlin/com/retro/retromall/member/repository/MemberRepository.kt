package com.retro.retromall.member.repository

import com.retro.retromall.member.domain.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<MemberEntity, Long>, MemberCustomRepository {
    fun findByOauthId(oauthId: String): MemberEntity?
}