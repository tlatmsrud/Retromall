package com.retro.retromall.member.infra.client.config

import com.retro.retromall.member.infra.client.properties.NaverProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

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
            .build()
    }

    @Bean
    fun naverApiClient(): WebClient {
        return WebClient.builder()
            .clientConnector(webClientConfig.getReactorHttpConnector())
            .baseUrl(naverProperties.apiUrl)
            .build()
    }
}