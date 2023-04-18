package com.retro.retromall.token.domain

import com.retro.retromall.member.domain.Member
import com.retro.retromall.member.enums.OAuthType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class TokenTest {

    private val REFRESH_TOKEN = "REFRESH_TOKEN"

    private val UPDATE_REFRESH_TOKEN = "UPDATE_REFRESH_TOKEN"
    @Test
    fun updateRefreshToken() {
        val member = Member(OAuthType.NAVER, "1","tlatmsrud@naver.com","심승경","심드류카네기","imgUrl")
        val token = Token(member, REFRESH_TOKEN, 100L)

        token.updateRefreshToken(UPDATE_REFRESH_TOKEN)

        assertThat(token.refreshToken).isEqualTo(UPDATE_REFRESH_TOKEN)

    }
}