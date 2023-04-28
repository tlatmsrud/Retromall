package com.retro.retromall.auth.client.dto

class OAuthAuthorizationCode(
    val code: String?,
    val state: String?,
    val error: String?,
    val errorDescription: String?
)