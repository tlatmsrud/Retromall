package com.retro.retromall.auth.client.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:properties/\${spring.profiles.active}/kakao/kakao.properties")
class KakaoProperties(
    @Value("\${kakao_client-id}")
    val clientId: String,

    @Value("\${kakao_client-secret}")
    val clientSecret: String,

    @Value("\${kakao_authorize-url}")
    val authorizeUrl: String,

    @Value("\${kakao_authorization-grant-type}")
    val authorizationGrantType: String,

    @Value("\${kakao_api-url}")
    val apiUrl: String,

    @Value("\${kakao_token-uri}")
    val tokenUri: String,

    @Value("\${kakao_user-info-uri}")
    val userInfoUri: String,

    @Value("\${kakao_redirect-uri}")
    val redirectUri: String,

    @Value("\${kakao_secure-resource}")
    val secureResource: Boolean
)