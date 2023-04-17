package com.retro.retromall.member.dto

data class LoginResponse(
    val refreshToken: String,
    val attributes: Attributes,
) {
    constructor(refreshToken: String, memberAttributes: MemberAttributes, tokenAttributes: TokenAttributes) :
            this(refreshToken, Attributes(memberAttributes, tokenAttributes))

    data class Attributes(
        val memberAttributes: MemberAttributes,
        val tokenAttributes: TokenAttributes
    )
}
