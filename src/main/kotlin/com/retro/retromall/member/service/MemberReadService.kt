package com.retro.retromall.member.service

import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.infra.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberReadService(
    private val memberRepository: MemberRepository
) {
    fun getMember(id: Long): Member {
        return memberRepository.findByIdOrNull(id) ?: throw IllegalStateException("해당 유저를 찾을 수 없습니다.")
    }
}