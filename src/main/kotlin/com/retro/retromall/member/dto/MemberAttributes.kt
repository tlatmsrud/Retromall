package com.retro.retromall.member.dto

data class MemberAttributes(
    var id: Long? = null,
    var nickName: String? = null,
    var profileImageUrl: String? = null,
    var roles: String? = null,
    var permissions: String? = null,
)