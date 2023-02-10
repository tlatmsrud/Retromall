package com.retro.retromall.member.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class OAuth2Type {
    KAKAO, NAVER;

    @JsonCreator
    fun create(value: String): OAuth2Type {
        for (oauth in OAuth2Type.values()) {
            if (oauth.name == value)
                return oauth
        }

        throw IllegalArgumentException("지원하지 않는 OAuth2 타입 입니다.")
    }
}