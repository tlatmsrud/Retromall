package com.retro.retromall.member.domain

import com.retro.retromall.member.enums.Role
import java.util.stream.Collectors
import javax.persistence.*

@Entity
@Table(name = "tb_member")
class Member(
    oauth2Id: String,
    email: String?,
    nickname: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    private var id: Long? = null

    @Column(name = "oauth2_id", unique = true, nullable = false)
    private val oauth2Id: String = oauth2Id

    @Column(name = "email", unique = true)
    private val email: String? = email

    @Column(name = "nickname", unique = false, nullable = false)
    private val nickname: String = nickname

    @ElementCollection(fetch = FetchType.EAGER)
    private val roles: MutableList<Role> = mutableListOf()

//    fun getAuthorities(): MutableCollection<out GrantedAuthority> {
//        return this.roles.stream()
//            .map { role -> SimpleGrantedAuthority(role.name) }
//            .collect(Collectors.toList())
//    }

    fun getAuthorities(): MutableCollection<Role> {
        return this.roles
    }

    fun getUsername(): String {
        return nickname
    }

    fun isAccountNonExpired(): Boolean {
        return true
    }

    fun isAccountNonLocked(): Boolean {
        return true
    }

    fun isCredentialsNonExpired(): Boolean {
        return true
    }

    fun isEnabled(): Boolean {
        return true
    }
}