package com.retro.retromall.member.infra.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.retromall.member.dto.LoginRequest
import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.client.naver.NaverTokenRequest
import com.retro.retromall.member.infra.client.naver.NaverTokenResponse
import com.retro.retromall.member.infra.client.naver.NaverUserInfoResponse
import com.retro.retromall.member.infra.client.properties.NaverProperties
import com.retro.retromall.member.support.OAuthMemberAttributesProvider
import com.retro.util.WebClientUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class OAuth2WebClientNaverImpl (
    @Qualifier("OAuthNaverMemberAttributesProvider")
    private val oAuthMemberAttributesProvider: OAuthMemberAttributesProvider,
    private val objectMapper: ObjectMapper,
    naverProperties: NaverProperties,
    naverAuthClient: WebClient,
    naverApiClient: WebClient,
) : OAuth2WebClient {

    private val logger: Logger = LoggerFactory.getLogger(OAuth2WebClientNaverImpl::class.java)
    private val properties = naverProperties
    private val authWebClient = naverAuthClient
    private val apiWebClient = naverApiClient

    override fun requestOAuthToken(loginRequest: LoginRequest): OAuthTokenAttributes {
        val naverTokenRequest = NaverTokenRequest(
            grantType = properties.authorizationGrantType,
            clientId = properties.clientId,
            clientSecret = properties.clientSecret,
            code = loginRequest.authorizationCode,
            state = loginRequest.state!!,
        )
        val parameters = WebClientUtils.convertParameters(naverTokenRequest, objectMapper)

        logger.info("Request Naver AccessToken")
        val response = authWebClient.post()
            .uri { uriBuilder -> uriBuilder.path(properties.tokenUri).build() }
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromFormData(parameters))
            .retrieve()
            .onStatus({ it != HttpStatus.OK }, {
                it.createException().flatMap { err ->
                    return@flatMap Mono.error(IllegalStateException(err.responseBodyAsString))
                }
            })
            .bodyToMono(NaverTokenResponse::class.java)
            .block()
        return OAuthTokenAttributes(
            tokenType = response!!.tokenType,
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            accessTokenExpiresIn = response.expires_in,
            refreshTokenExpiresIn = 0,
            scope = null
            )
    }

    override fun requestOAuthUserInfo(attributes: OAuthTokenAttributes): OAuthMemberAttributes {
        val response = apiWebClient.get()
            .uri { uriBuilder -> uriBuilder.path(properties.userInfoUri).build() }
            .header(HttpHeaders.AUTHORIZATION, attributes.tokenType + " " + attributes.accessToken)
            .retrieve().onStatus({ it != HttpStatus.OK },
                {
                    it.createException().flatMap { err ->
                        throw IllegalStateException(err.responseBodyAsString)
                    }
                })
            .bodyToMono(NaverUserInfoResponse::class.java)
            .block()

        return oAuthMemberAttributesProvider.createOAuthMemberAttributes(response!!)
    }

    override fun getOAuthType(): OAuthType {
        return OAuthType.NAVER
    }

    override fun getClient(): OAuth2WebClient {
        return this
    }
}