package com.retro.retromall.member.infra.client.properties

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:properties/\${spring.profiles.active}/naver.properties")
class NaverProperties {
}