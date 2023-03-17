package com.retro.config

import com.retro.common.MemberAttributeResolver
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val memberAttributeResolver: MemberAttributeResolver,
    private val environment: Environment
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(memberAttributeResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        val activeProfiles = environment.activeProfiles
        if (activeProfiles.contains("local")) {
            registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600)
        } else {
            registry.addMapping("/**")
                .allowedOrigins("https://retromall.herokuapp.com")
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600)
        }
    }
}