package com.retro.retromall.member.domain

import com.retro.retromall.member.enums.OAuthType
import javax.persistence.*

@Entity
@Table(name = "tb_member")
class Member(
    oAuthType: OAuthType,
    oauthId: String,
    email: String?,
    nickname: String?
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_type", nullable = false)
    val oauthType: OAuthType = oAuthType

    @Column(name = "oauth_id", unique = true, nullable = false)
    val oauthId: String = oauthId

    @Column(name = "email", unique = true)
    val email: String? = email

    @Column(name = "nickname", unique = false)
    val nickname: String? = nickname

//    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
//    val products: MutableList<Product> = mutableListOf()

//    @ElementCollection(fetch = FetchType.EAGER)
//    val roles: MutableList<Role> = mutableListOf()

//    fun getAuthorities(): MutableCollection<out GrantedAuthority> {
//        return this.roles.stream()
//            .map { role -> SimpleGrantedAuthority(role.name) }
//            .collect(Collectors.toList())
//    }

//    fun getAuthorities(): MutableCollection<Role> {
//        return this.roles
//    }

    fun getUsername(): String? {
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