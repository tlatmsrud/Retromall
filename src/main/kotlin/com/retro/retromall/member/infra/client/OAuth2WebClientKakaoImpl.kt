package com.retro.retromall.member.infra.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.retromall.member.dto.LoginRequest
import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.client.properties.KakaoProperties
import com.retro.retromall.member.infra.client.dto.kakao.KakaoTokenRequest
import com.retro.retromall.member.infra.client.dto.kakao.KakaoTokenResponse
import com.retro.retromall.member.infra.client.dto.kakao.KakaoUserInfoRequest
import com.retro.retromall.member.infra.client.dto.kakao.KakaoUserInfoResponse
import com.retro.retromall.member.support.OAuthMemberAttributesProvider
import com.retro.retromall.member.support.OAuthTokenAttributesProvider
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
class OAuth2WebClientKakaoImpl(
    private val memberAttributesProvider: OAuthMemberAttributesProvider<KakaoUserInfoResponse>,
    private val tokenAttributesProvider: OAuthTokenAttributesProvider<KakaoTokenResponse>,
    private val objectMapper: ObjectMapper,
    private val properties: KakaoProperties,
    @Qualifier("kakaoAuthClient")
    private val authWebClient: WebClient,
    @Qualifier("kakaoApiClient")
    private val apiWebClient: WebClient
) : OAuth2WebClient {
    private val logger: Logger = LoggerFactory.getLogger(OAuth2WebClientKakaoImpl::class.java)

    override fun getAccessToken(loginRequest: LoginRequest): OAuthTokenAttributes {
        val kakaoTokenRequest = KakaoTokenRequest(
            grantType = properties.authorizationGrantType,
            clientId = properties.clientId,
            clientSecret = properties.clientSecret,
            redirectUri = properties.redirectUri,
            code = loginRequest.authorizationCode
        )
        val parameters = WebClientUtils.convertParameters(kakaoTokenRequest, objectMapper)

        logger.info("Request Kakao AccessToken")
        val response = authWebClient.post()
            .uri { uriBuilder -> uriBuilder.path(properties.tokenUri).build() }
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(parameters))
            .retrieve()
            .onStatus({ it != HttpStatus.OK }, {
                it.createException().flatMap { err ->
                    return@flatMap Mono.error(IllegalStateException(err.responseBodyAsString))
                }
            })
            .bodyToMono(KakaoTokenResponse::class.java)
            .block()
        return tokenAttributesProvider.createOAuthTokenAttributes(response!!)
    }

    override fun getUserInfo(attributes: OAuthTokenAttributes): OAuthMemberAttributes {
        val kakaoUserInfoRequest =
            KakaoUserInfoRequest(secureResource = properties.secureResource, scope = properties.scope)
        val parameters = WebClientUtils.convertParameters(kakaoUserInfoRequest, objectMapper)
        val response = apiWebClient.post()
            .uri { uriBuilder -> uriBuilder.path(properties.userInfoUri).build() }
            .header(HttpHeaders.AUTHORIZATION, attributes.tokenType + " " + attributes.accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(parameters))
            .retrieve().onStatus({ it != HttpStatus.OK },
                {
                    it.createException().flatMap { err ->
                        return@flatMap Mono.error(IllegalStateException(err.responseBodyAsString))
                    }
                })
            .bodyToMono(KakaoUserInfoResponse::class.java)
            .block()

        return memberAttributesProvider.createOAuthMemberAttributes(response!!)
    }

    override fun getOAuthType(): OAuthType {
        return OAuthType.KAKAO
    }

    override fun getClient(): OAuth2WebClient {
        return this
    }
}