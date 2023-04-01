package com.retro.retromall.member.infra.client.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:properties/\${spring.profiles.active}/naver.properties")
class NaverProperties (

    @Value("\${client-id}")
    val clientId: String,

    @Value("\${client-secret}")
    val clientSecret: String,

    @Value("\${authorize-url}")
    val authorizeUrl: String,

    @Value("\${api-url}")
    val apiUrl: String,

    @Value("\${authorization-grant-type}")
    val authorizationGrantType: String,

    @Value("\${token-uri}")
    val tokenUri: String,

    @Value("\${user-info-uri}")
    val userInfoUri: String

    )