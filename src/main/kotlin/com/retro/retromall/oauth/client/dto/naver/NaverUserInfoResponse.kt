package com.retro.retromall.oauth.client.dto.naver

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverUserInfoResponse(
    @field:JsonProperty("resultcode")
    val resultCode: String,

    @field:JsonProperty("message")
    val message: String,

    @field:JsonProperty("response", required = false)
    val naverAccount: NaverAccount?
) {
    data class NaverAccount(

        @field:JsonProperty("id", required = true)
        val id: String?,

        @field:JsonProperty("nickname", required = false)
        val nickname: String?,

        @field:JsonProperty("profile_image", required = false)
        val profileImageUrl: String?,

        @field:JsonProperty("age", required = false)
        val age: String?,

        @field:JsonProperty("gender", required = false)
        val gender: String?,

        @field:JsonProperty("email", required = false)
        val email: String?,

        @field:JsonProperty("mobile", required = false)
        val mobile: String?,

        @field:JsonProperty("name", required = false)
        val name: String?,

        @field:JsonProperty("birthday", required = false)
        val birthday: String?,

        @field:JsonProperty("birthyear", required = false)
        val birthYear: String?,
        )
}