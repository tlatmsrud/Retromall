package com.retro.retromall.member.infra.client.dto.kakao

import com.fasterxml.jackson.annotation.JsonProperty

class KakaoUserInfoRequest(
    secureResource: Boolean,
    scope: List<String>
) {
    @JsonProperty("secure_resource")
    val secureResource: Boolean = secureResource

    @JsonProperty("property_keys")
    val propertyKeys: String = scope.joinToString(prefix = "[\"", separator = "\", \"", postfix = "\"]")
}