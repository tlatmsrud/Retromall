package com.retro.util

import org.springframework.http.ResponseCookie

class HttpUtils {

    companion object{
        fun generateCookie(name : String, value : String, path : String, day : Long) : ResponseCookie {

            return ResponseCookie.from(name, value)
                .path(path)
                .secure(true)
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * day)
                .build()
        }
    }
}