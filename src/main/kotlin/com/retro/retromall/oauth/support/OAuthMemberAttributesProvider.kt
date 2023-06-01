package com.retro.retromall.oauth.support

import com.retro.retromall.oauth.client.dto.OAuthMemberAttributes

interface OAuthMemberAttributesProvider<T> {
    fun createOAuthMemberAttributes(data: T): OAuthMemberAttributes
}