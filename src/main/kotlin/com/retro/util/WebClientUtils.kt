package com.retro.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.exception.OAuthException
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

class WebClientUtils {
    companion object {
        fun convertParameters(dto: Any, objectMapper: ObjectMapper): MultiValueMap<String, String> {
            val params = LinkedMultiValueMap<String, String>()
            val buffer = objectMapper.convertValue(dto, object : TypeReference<Map<String, String>>() {})
            params.setAll(buffer)
            return params
        }

        fun <T> handleWebClientErrors(request: Mono<T>): Mono<T> {
            return request.onErrorResume { error ->
                if (error is WebClientResponseException) {
                    Mono.error(OAuthException(error.responseBodyAsString))
                } else {
                    Mono.error(error)
                }
            }
        }
    }
}