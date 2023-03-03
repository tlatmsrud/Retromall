package com.retro.retromall.member.domain

import com.retro.retromall.member.enums.OAuthType
import javax.persistence.*

@Entity
@Table(name = "tb_member")
class Member(
    oAuthType: OAuthType,
    oauthId: String,
    email: String?,
    name: String?,
    nickname: String?,
    profileImageUrl: String?
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_type", nullable = false)
    val oauthType: OAuthType = oAuthType

    @Column(name = "oauth_id", length = 255, unique = true, nullable = false)
    val oauthId: String = oauthId

    @Column(name = "email", length = 100, unique = true)
    var email: String? = email

    @Column(name = "member_name", length = 50, unique = false)
    var name = name

    @Column(name = "nickname", length = 100, unique = false)
    var nickname: String? = nickname

    @Column(name = "profile_image_url", length = 255)
    var profileImageUrl = profileImageUrl
}