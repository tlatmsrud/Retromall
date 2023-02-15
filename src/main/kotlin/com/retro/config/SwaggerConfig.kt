package com.retro.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile(value = ["!prd"])
class SwaggerConfig {
    @Bean
    fun api() : OpenAPI {
        val info = Info().version("0.5")
            .title("Retromall")
            .description("Retromall API Description")
        return OpenAPI().info(info)
    }
}