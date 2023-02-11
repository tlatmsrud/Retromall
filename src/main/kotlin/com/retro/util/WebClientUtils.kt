package com.retro.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

class WebClientUtils {
    companion object {
        fun convertParameters(dto: Any, objectMapper: ObjectMapper): MultiValueMap<String, String> {
            val params = LinkedMultiValueMap<String, String>()
            val buffer = objectMapper.convertValue(dto, object : TypeReference<Map<String, String>>(){})
            params.setAll(buffer)
            return params
        }
    }
}