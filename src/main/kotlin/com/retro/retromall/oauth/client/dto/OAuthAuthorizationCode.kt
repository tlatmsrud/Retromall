package com.retro.retromall.oauth.client.dto

class OAuthAuthorizationCode(
    val code: String?,
    val state: String?,
    val error: String?,
    val errorDescription: String?
)