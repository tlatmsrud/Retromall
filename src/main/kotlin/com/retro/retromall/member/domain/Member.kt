package com.retro.retromall.member.domain

import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.role.domain.Role
import javax.persistence.*

@Entity
@Table(name = "tb_member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_type", nullable = false)
    val oauthType: OAuthType,

    @Column(name = "oauth_id", length = 255, unique = true, nullable = false)
    val oauthId: String,

    @Column(name = "email", length = 100, unique = true)
    var email: String?,

    @Column(name = "member_name", length = 50, unique = false)
    var name: String?,

    @Column(name = "nickname", length = 100, unique = false)
    var nickname: String?,

    @Column(name = "profile_image_url", length = 255)
    var profileImageUrl: String?,

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val roles: MutableSet<MemberRole>
) {
    constructor(
        oAuthType: OAuthType,
        oauthId: String,
        email: String?,
        name: String?,
        nickname: String?,
        profileImageUrl: String?
    ) : this(
        null, oAuthType, oauthId, email, name, nickname, profileImageUrl, mutableSetOf()
    )

    fun addRole(role: Role) {
        this.roles.add(MemberRole(role, this))
    }
}