package com.retro.retromall.member.support

import com.retro.retromall.member.dto.OAuthTokenAttributes

interface OAuthTokenAttributesProvider<T> {
    fun createOAuthTokenAttributes(data: T): OAuthTokenAttributes
}