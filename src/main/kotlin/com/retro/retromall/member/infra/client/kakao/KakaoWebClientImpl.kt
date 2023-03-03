package com.retro.retromall.member.infra.client.kakao

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.retromall.member.dto.OAuthMemberAttributes
import com.retro.retromall.member.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.infra.client.OAuth2WebClient
import com.retro.retromall.member.infra.client.OAuthMemberAttributeFactory
import com.retro.retromall.member.infra.client.config.KakaoProperties
import com.retro.util.WebClientUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class KakaoWebClientImpl(
    kakaoAuthClient: WebClient,
    kakaoApiClient: WebClient,
    kakaoProperties: KakaoProperties,
    private val objectMapper: ObjectMapper
    ) : OAuth2WebClient {
    private val logger: Logger = LoggerFactory.getLogger(KakaoWebClientImpl::class.java)
    private val authWebClient = kakaoAuthClient
    private val apiWebClient = kakaoApiClient
    private val properties = kakaoProperties


    override fun getToken(authorizationCode: String): OAuthTokenAttributes {
        val kakaoTokenRequest = KakaoTokenRequest(
            grantType = properties.authorizationGrantType,
            clientId = properties.clientId,
            clientSecret = properties.clientSecret,
            redirectUri = properties.redirectUri,
            code = authorizationCode
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
        return OAuthTokenAttributes(
            tokenType = response!!.tokenType,
            accessToken = response.accessToken,
            accessTokenExpiresIn = response.accessTokenExpiresIn,
            refreshToken = response.refreshToken,
            refreshTokenExpiresIn = response.refreshTokenExpiresIn,
            scope = response.scope
        )
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

        return OAuthMemberAttributeFactory.createOauthMemberAttributes(
            oAuthType = OAuthType.KAKAO,
            response = response!!
        )
    }

    override fun getOAuthType(): OAuthType {
        return OAuthType.KAKAO
    }

    override fun getClient(): OAuth2WebClient {
        return this
    }
}