package com.retro.retromall.oauth.support

import com.retro.retromall.oauth.client.dto.OAuthTokenAttributes

interface OAuthTokenAttributesProvider<T> {
    fun createOAuthTokenAttributes(data: T): OAuthTokenAttributes
}