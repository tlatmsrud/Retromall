package com.retro.retromall.member.infra.repository

import com.retro.retromall.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface MemberRepository : JpaRepository<Member, String> {
    fun findByOauth2Id(oauthId: String): Optional<Member>
}