package com.retro.retromall.auth.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.exception.OAuthException
import com.retro.retromall.auth.client.dto.OAuthMemberAttributes
import com.retro.retromall.auth.client.dto.OAuthTokenAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.auth.client.dto.OAuthAuthorizationCode
import com.retro.retromall.auth.client.dto.kakao.*
import com.retro.retromall.auth.client.properties.KakaoProperties
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

    override fun getAccessToken(oAuthAuthorizationCode: OAuthAuthorizationCode): OAuthTokenAttributes {
        val requestDto = KakaoTokenRequestDto(
            grantType = properties.authorizationGrantType,
            clientId = properties.clientId,
            clientSecret = properties.clientSecret,
            redirectUri = properties.redirectUri,
            code = oAuthAuthorizationCode.code!!
        )
        val parameters = WebClientUtils.convertParameters(requestDto, objectMapper)

        val response = WebClientUtils.handleWebClientErrors(authWebClient.post()
            .uri { uriBuilder -> uriBuilder.path(properties.tokenUri).build() }
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(parameters))
            .retrieve()
            .bodyToMono(KakaoTokenResponse::class.java))
            .block()
        return response?.let { tokenAttributesProvider.createOAuthTokenAttributes(it) }
            ?: throw OAuthException("카카오 토큰 요청 응답 값이 Null 입니다.")
    }

    override fun getUserInfo(attributes: OAuthTokenAttributes): OAuthMemberAttributes {
        val requestDto =
            KakaoUserInfoRequestDto(secureResource = properties.secureResource)
        val parameters = WebClientUtils.convertParameters(requestDto, objectMapper)
        val response = WebClientUtils.handleWebClientErrors(apiWebClient.post()
            .uri { uriBuilder -> uriBuilder.path(properties.userInfoUri).build() }
            .header(HttpHeaders.AUTHORIZATION, attributes.tokenType + " " + attributes.accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(parameters))
            .retrieve()
            .bodyToMono(KakaoUserInfoResponse::class.java))
            .block()

        return response?.let { memberAttributesProvider.createOAuthMemberAttributes(it) }
            ?: throw OAuthException("카카오 유저정보 요청 응답 값이 Null 입니다.")
    }

    override fun getOAuthType(): OAuthType {
        return OAuthType.KAKAO
    }

    override fun getClient(): OAuth2WebClient {
        return this
    }
}