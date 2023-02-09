package com.retro.retromall.member.domain.repository

import com.retro.retromall.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, String> {
}