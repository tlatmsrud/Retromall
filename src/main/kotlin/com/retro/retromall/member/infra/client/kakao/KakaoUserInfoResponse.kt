package com.retro.retromall.member.infra.client.kakao

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

class KakaoUserInfoResponse(
    @field:JsonProperty("id")
    val id: Long,

    @field:JsonProperty("has_signed_up")
    val hasSignedUp: Boolean,

    @field:JsonProperty("connected_at")
    val connectedAt: LocalDateTime,

    @field:JsonProperty("synched_at")
    val synchedAt: LocalDateTime,

    @field:JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount
) {
    inner class KakaoAccount(
        @field:JsonProperty("profile_needs_agreement")
        val profileNeedsAgreement: Boolean,

        @field:JsonProperty("profile_nickname_needs_agreement")
        val profileNicknameNeedsAgreement: Boolean,

        @field:JsonProperty("profile_image_needs_agreement")
        val profileImageNeedsAgreement: Boolean,

        @field:JsonProperty("profile")
        val profile: Profile,

        @field:JsonProperty("name_needs_agreement")
        val nameNeedsAgreement: Boolean,

        @field:JsonProperty("name")
        val name: String,

        @field:JsonProperty("email_needs_agreement")
        val emailNeedsAgreement: Boolean,

        @field:JsonProperty("is_email_valid")
        val isEmailValid: Boolean,

        @field:JsonProperty("email")
        val email: String,

        @field:JsonProperty("age_range_needs_agreement")
        val ageRangeNeedsAgreement: Boolean,

        @field:JsonProperty("age_range")
        val ageRange: String,

        @field:JsonProperty("birth_year_needs_agreement")
        val birthYearNeedsAgreement: Boolean,

        @field:JsonProperty("birthday")
        val birthday: String,

        @field:JsonProperty("gender_needs_agreement")
        val genderNeedsAgreement: Boolean,

        @field:JsonProperty("gender")
        val gender: String,

        @field:JsonProperty("phone_number_needs_agreement")
        val phoneNumberNeedsAgreement: Boolean,

        @field:JsonProperty("phone_number")
        val phoneNumber: String
    ) {
        inner class Profile(
            @field:JsonProperty("nickname")
            val nickName: String,

            @field:JsonProperty("profile_image_url")
            val profileImageUrl: String
        )
    }
}