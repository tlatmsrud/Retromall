package com.retro.retromall.member.repository

import com.retro.retromall.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {
    fun findByOauthId(oauthId: String): Member?
}