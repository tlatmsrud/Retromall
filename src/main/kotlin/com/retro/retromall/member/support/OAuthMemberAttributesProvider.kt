package com.retro.retromall.member.support

import com.retro.retromall.member.dto.OAuthMemberAttributes

interface OAuthMemberAttributesProvider<T> {
    fun createOAuthMemberAttributes(data: T): OAuthMemberAttributes
}