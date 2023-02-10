package com.retro.retromall.member.infra.client.kakao

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class KakaoUserInfoResponse(
    @field:JsonProperty("id")
    val id: Long,

    @field:JsonProperty("has_signed_up")
    val hasSignedUp: Boolean?,

    @field:JsonProperty("connected_at", required = false)
    val connectedAt: LocalDateTime?,

    @field:JsonProperty("synched_at", required = false)
    val synchedAt: LocalDateTime?,

    @field:JsonProperty("kakao_account", required = false)
    val kakaoAccount: KakaoAccount?
) {
    data class KakaoAccount(
        @field:JsonProperty("profile_needs_agreement", required = false)
        val profileNeedsAgreement: Boolean?,

        @field:JsonProperty("profile_nickname_needs_agreement", required = false)
        val profileNicknameNeedsAgreement: Boolean?,

        @field:JsonProperty("profile_image_needs_agreement", required = false)
        val profileImageNeedsAgreement: Boolean?,

        @field:JsonProperty("profile", required = false)
        val profile: Profile?,

        @field:JsonProperty("name_needs_agreement", required = false)
        val nameNeedsAgreement: Boolean?,

        @field:JsonProperty("name", required = false)
        val name: String?,

        @field:JsonProperty("email_needs_agreement", required = false)
        val emailNeedsAgreement: Boolean?,

        @field:JsonProperty("is_email_valid", required = false)
        val isEmailValid: Boolean?,

        @field:JsonProperty("email", required = false)
        val email: String?,

        @field:JsonProperty("age_range_needs_agreement", required = false)
        val ageRangeNeedsAgreement: Boolean?,

        @field:JsonProperty("age_range", required = false)
        val ageRange: String?,

        @field:JsonProperty("birth_year_needs_agreement", required = false)
        val birthYearNeedsAgreement: Boolean?,

        @field:JsonProperty("birthday", required = false)
        val birthday: String?,

        @field:JsonProperty("gender_needs_agreement", required = false)
        val genderNeedsAgreement: Boolean?,

        @field:JsonProperty("gender", required = false)
        val gender: String?,

        @field:JsonProperty("phone_number_needs_agreement", required = false)
        val phoneNumberNeedsAgreement: Boolean?,

        @field:JsonProperty("phone_number", required = false)
        val phoneNumber: String?
    ) {
        data class Profile(
            @field:JsonProperty("nickname", required = false)
            val nickName: String?,

            @field:JsonProperty("profile_image_url", required = false)
            val profileImageUrl: String?
        )
    }
}