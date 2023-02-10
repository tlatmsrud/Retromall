package com.retro.retromall.member.infra.client.config

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
class OAuthWebClientConfig(
    private val kakaoProperties: KakaoProperties
) {
    @Bean
    fun kakaoAuthClient(): WebClient {
        return WebClient.builder()
            .clientConnector(getReactorHttpConnector())
            .baseUrl(kakaoProperties.authorizeUrl)
            .build()
    }

    @Bean
    fun kakaoApiClient(): WebClient {
        return WebClient.builder()
            .clientConnector(getReactorHttpConnector())
            .baseUrl(kakaoProperties.apiUrl)
            .build()
    }

    private fun getReactorHttpConnector(): ReactorClientHttpConnector {
        val sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        val httpClient = HttpClient.create().secure { t -> t.sslContext(sslContext) }
        return ReactorClientHttpConnector(httpClient)
    }
}