package com.retro.retromall.member.service

import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.domain.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        return memberRepository.findByIdOrNull(username)
            ?.let {
            createUserDetails(it)
        } ?: throw UsernameNotFoundException("해당 유저를 찾을 수 없습니다.")
    }

    private fun createUserDetails(member: Member): UserDetails {
        return User.builder()
            .username(member.username)
            .password(passwordEncoder.encode(member.getPassword()))
            .roles(member.roles.toString())
            .build()
    }
}