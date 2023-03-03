package com.retro.retromall.member.infra.client.config

import com.retro.retromall.member.infra.client.properties.NaverProperties
import org.springframework.context.annotation.Configuration

@Configuration
class NaverWebClientConfig(
    private val webClientConfig: WebClientConfig,
    private val naverProperties: NaverProperties
) {
}