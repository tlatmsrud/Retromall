package com.retro.retromall.member.infra.client.config

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import reactor.netty.http.client.HttpClient

@Configuration
class WebClientConfig {
    internal fun getReactorHttpConnector(): ReactorClientHttpConnector {
        val sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        val httpClient = HttpClient.create().secure { t -> t.sslContext(sslContext) }
        return ReactorClientHttpConnector(httpClient)
    }
}