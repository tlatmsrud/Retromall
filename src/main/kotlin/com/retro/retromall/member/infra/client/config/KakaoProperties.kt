package com.retro.retromall.member.infra.client.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:properties/\${spring.profiles.active}/kakao.properties")
class KakaoProperties(
    @Value("\${client-id}")
    val clientId: String,

    @Value("\${client-secret}")
    val clientSecret: String,

    @Value("\${authorize-url}")
    val authorizeUrl: String,

    @Value("\${authorization-grant-type}")
    val authorizationGrantType: String,

    @Value("\${api-url}")
    val apiUrl: String,

    @Value("\${token-uri}")
    val tokenUri: String,

    @Value("\${user-info-uri}")
    val userInfoUri: String,

    @Value("\${redirect-uri}")
    val redirectUri: String,

    @Value("\${scope}")
    val scope: String,

    @Value("\${secure-resource}")
    val secureResource: Boolean
)