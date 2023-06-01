package com.retro.retromall.oauth.client.config

import com.retro.retromall.oauth.client.properties.NaverProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Configuration
class NaverWebClientConfig(
    private val webClientConfig: WebClientConfig,
    private val naverProperties: NaverProperties
) {

    @Bean
    fun naverAuthClient(): WebClient {
        return WebClient.builder()
            .clientConnector(webClientConfig.getReactorHttpConnector())
            .baseUrl(naverProperties.authorizeUrl)
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
    fun naverApiClient(): WebClient {
        return WebClient.builder()
            .clientConnector(webClientConfig.getReactorHttpConnector())
            .baseUrl(naverProperties.apiUrl)
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