package com.retro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RetromallApplication

fun main(args: Array<String>) {
    runApplication<RetromallApplication>(*args)
}
