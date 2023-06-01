package com.retro.retromall.oauth.client.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:properties/\${spring.profiles.active}/naver/naver.properties")
class NaverProperties(

    @Value("\${naver_client-id}")
    val clientId: String,

    @Value("\${naver_client-secret}")
    val clientSecret: String,

    @Value("\${naver_authorize-url}")
    val authorizeUrl: String,

    @Value("\${naver_api-url}")
    val apiUrl: String,

    @Value("\${naver_authorization-grant-type}")
    val authorizationGrantType: String,

    @Value("\${naver_token-uri}")
    val tokenUri: String,

    @Value("\${naver_user-info-uri}")
    val userInfoUri: String

)