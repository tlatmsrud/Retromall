package com.retro.retromall.member.infra.repository

import com.retro.retromall.member.dto.MemberAttributes

interface MemberCustomRepository {
    fun selectPermissionsByMemberId(id: Long): MemberAttributes?
}