package com.retro.retromall.auth.client.config

import com.retro.retromall.auth.client.properties.KakaoProperties
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient

@Configuration
class KakaoWebClientConfig(
    private val webClientConfig: WebClientConfig,
    private val kakaoProperties: KakaoProperties
) {
    @Bean
    fun kakaoAuthClient(): WebClient {
        return WebClient.builder()
            .clientConnector(webClientConfig.getReactorHttpConnector())
            .baseUrl(kakaoProperties.authorizeUrl)
            .filter(ExchangeFilterFunction.ofResponseProcessor { clientResponse ->
                if (clientResponse.statusCode().isError) {
                    clientResponse.createException().flatMap { Mono.error(it) }
                } else {
                    Mono.just(clientResponse)
                }
            })
            .build()
    }

    @Bean
    fun kakaoApiClient(): WebClient {
        return WebClient.builder()
            .clientConnector(webClientConfig.getReactorHttpConnector())
            .baseUrl(kakaoProperties.apiUrl)
            .filter(ExchangeFilterFunction.ofResponseProcessor { clientResponse ->
                if (clientResponse.statusCode().isError) {
                    clientResponse.createException().flatMap { Mono.error(it) }
                } else {
                    Mono.just(clientResponse)
                }
            })
            .build()
    }
}