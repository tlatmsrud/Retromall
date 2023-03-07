package com.retro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class RetromallApplication

fun main(args: Array<String>) {
    runApplication<RetromallApplication>(*args)
}
