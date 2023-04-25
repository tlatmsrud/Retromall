package com.retro.retromall.auth.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.exception.OAuthException
import com.retro.retromall.auth.client.dto.OAuthMemberAttributes
import com.retro.retromall.auth.client.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.auth.client.dto.OAuthAuthorizationCode
import com.retro.retromall.auth.client.dto.naver.NaverCodeDto
import com.retro.retromall.auth.client.dto.naver.NaverTokenRequest
import com.retro.retromall.auth.client.dto.naver.NaverTokenResponse
import com.retro.retromall.auth.client.dto.naver.NaverUserInfoResponse
import com.retro.retromall.auth.client.properties.NaverProperties
import com.retro.retromall.member.support.OAuthMemberAttributesProvider
import com.retro.retromall.member.support.OAuthTokenAttributesProvider
import com.retro.util.WebClientUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Component
class OAuth2WebClientNaverImpl(
    private val memberAttributesProvider: OAuthMemberAttributesProvider<NaverUserInfoResponse>,
    private val tokenAttributesProvider: OAuthTokenAttributesProvider<NaverTokenResponse>,
    private val objectMapper: ObjectMapper,
    private val properties: NaverProperties,
    @Qualifier("naverAuthClient")
    private val authWebClient: WebClient,
    @Qualifier("naverApiClient")
    private val apiWebClient: WebClient,
) : OAuth2WebClient {
    private val logger: Logger = LoggerFactory.getLogger(OAuth2WebClientNaverImpl::class.java)

    override fun getAccessToken(oAuthAuthorizationCode: OAuthAuthorizationCode): OAuthTokenAttributes {
        if (oAuthAuthorizationCode !is NaverCodeDto) throw IllegalArgumentException()
        val naverTokenRequest = NaverTokenRequest(
            grantType = properties.authorizationGrantType,
            clientId = properties.clientId,
            clientSecret = properties.clientSecret,
            code = oAuthAuthorizationCode.code!!,
            state = oAuthAuthorizationCode.state!!,
        )
        val parameters = WebClientUtils.convertParameters(naverTokenRequest, objectMapper)

        logger.info("Request Naver AccessToken")
        val response = WebClientUtils.handleWebClientErrors(authWebClient.post()
            .uri { uriBuilder -> uriBuilder.path(properties.tokenUri).build() }
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromFormData(parameters))
            .retrieve()
            .bodyToMono(NaverTokenResponse::class.java))
            .block()
        return response?.let { tokenAttributesProvider.createOAuthTokenAttributes(it) }
            ?: throw OAuthException("네이버 토큰 요청 응답 값이 Null 입니다.")
    }

    override fun getUserInfo(attributes: OAuthTokenAttributes): OAuthMemberAttributes {
        val response = WebClientUtils.handleWebClientErrors(apiWebClient.get()
            .uri { uriBuilder -> uriBuilder.path(properties.userInfoUri).build() }
            .header(HttpHeaders.AUTHORIZATION, attributes.tokenType + " " + attributes.accessToken)
            .retrieve()
            .bodyToMono(NaverUserInfoResponse::class.java))
            .block()

        return response?.let { memberAttributesProvider.createOAuthMemberAttributes(it) }
            ?: throw OAuthException("카카오 유저정보 요청 응답 값이 Null 입니다.")
    }

    override fun getOAuthType(): OAuthType {
        return OAuthType.NAVER
    }

    override fun getClient(): OAuth2WebClient {
        return this
    }
}