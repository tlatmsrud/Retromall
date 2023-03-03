package com.retro.retromall.member.service

import com.retro.retromall.member.infra.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberReadService(
    private val memberRepository: MemberRepository
) {
}