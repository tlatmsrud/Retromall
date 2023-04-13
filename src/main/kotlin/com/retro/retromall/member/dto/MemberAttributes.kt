package com.retro.retromall.member.dto

data class MemberAttributes(
    val id: Long?,
    val role: String,
    val permissions: Set<String>?
)
