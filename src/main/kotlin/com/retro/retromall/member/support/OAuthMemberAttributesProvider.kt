package com.retro.retromall.member.support

import com.retro.retromall.member.dto.OAuthMemberAttributes

interface OAuthMemberAttributesProvider {
    fun createOAuthMemberAttributes(response: Any): OAuthMemberAttributes
}