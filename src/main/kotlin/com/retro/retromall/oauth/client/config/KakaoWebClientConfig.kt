package com.retro.retromall.oauth.client.config

import com.retro.retromall.oauth.client.properties.KakaoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

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