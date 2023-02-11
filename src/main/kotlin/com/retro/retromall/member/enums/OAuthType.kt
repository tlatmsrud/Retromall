package com.retro.retromall.member.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class OAuthType {
    KAKAO, NAVER;

    @JsonCreator
    fun create(value: String): OAuthType {
        for (oauth in OAuthType.values()) {
            if (oauth.name == value)
                return oauth
        }

        throw IllegalArgumentException("지원하지 않는 OAuth2 타입 입니다.")
    }
}