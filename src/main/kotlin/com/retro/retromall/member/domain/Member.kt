package com.retro.retromall.member.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors
import javax.persistence.*

@Entity
@Table(name = "tb_member")
class Member(
    id: String,
    password: String
) : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    private var id: String = id

    @Column(name = "password", nullable = false)
    private val password: String = password

    @ElementCollection(fetch = FetchType.EAGER)
    private val roles: MutableList<String> = mutableListOf()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roles.stream()
            .map { role -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return id
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}