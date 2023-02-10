package com.retro.retromall.member.infra.client.kakao

import com.fasterxml.jackson.annotation.JsonProperty

class KakaoUserInfoRequest(
    @field:JsonProperty("secure_resource")
    val secureResource: Boolean,

    @field:JsonProperty("property_keys")
    val scope: String
)