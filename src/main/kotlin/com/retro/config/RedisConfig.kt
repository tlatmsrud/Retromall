package com.retro.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class RedisConfig(
    @Value("\${spring.redis.port}") private val port: Int,
    @Value("\${spring.redis.host}") private val host: String,
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory? {
        return LettuceConnectionFactory(host, port)
    }
    @Bean
    fun redisTemplate(): RedisTemplate<String, Long> {
        val objectMapper = ObjectMapper()
        objectMapper.enable(DeserializationFeature.USE_LONG_FOR_INTS)
        val template = RedisTemplate<String, Long>()
        redisConnectionFactory()?.let { template.setConnectionFactory(it) }
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        template.setDefaultSerializer(GenericJackson2JsonRedisSerializer())
        return template
    }
}