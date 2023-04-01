package com.retro.retromall.member.infra.client.naver

import com.fasterxml.jackson.annotation.JsonProperty

class NaverUserInfoRequest(
    secureResource: Boolean,
    scope: List<String>
) {
    @JsonProperty("secure_resource")
    val secureResource: Boolean = secureResource

    @JsonProperty("property_keys")
    val propertyKeys: String = "[\"" + scope.joinToString { "," } + "\"]"
}