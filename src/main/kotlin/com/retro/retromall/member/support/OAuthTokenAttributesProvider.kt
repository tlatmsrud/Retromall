package com.retro.retromall.member.support

import com.retro.retromall.member.infra.client.dto.OAuthTokenAttributes

interface OAuthTokenAttributesProvider<T> {
    fun createOAuthTokenAttributes(data: T): OAuthTokenAttributes
}