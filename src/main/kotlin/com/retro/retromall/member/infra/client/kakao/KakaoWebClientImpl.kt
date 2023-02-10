package com.retro.retromall.member.infra.client.kakao

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.dto.OAuthAttributes
import com.retro.retromall.member.enums.OAuth2Type
import com.retro.retromall.member.infra.client.OAuth2WebClient
import com.retro.retromall.member.infra.client.config.KakaoProperties
import com.retro.retromall.util.WebClientUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Component
class KakaoWebClientImpl(
    kakaoAuthClient: WebClient,
    kakaoApiClient: WebClient,
    objectMapper: ObjectMapper,
    kakaoProperties: KakaoProperties,
) : OAuth2WebClient {
    private val logger: Logger = LoggerFactory.getLogger(KakaoWebClientImpl::class.java)
    private val objectMapper = objectMapper
    private val authWebClient = kakaoAuthClient
    private val apiWebClient = kakaoApiClient
    private val properties = kakaoProperties


    override fun getToken(authorizationCode: String): OAuthAttributes {
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
                    logger.error(err.message)
                    throw IllegalStateException(err.message)
                }
            })
            .bodyToMono(KakaoTokenResponse::class.java)
            .block()
        return OAuthAttributes(
            tokenType = response!!.tokenType,
            accessToken = response.accessToken,
            accessTokenExpiresIn = response.accessTokenExpiresIn,
            refreshToken = response.refreshToken,
            refreshTokenExpiresIn = response.refreshTokenExpiresIn,
            scope = response.scope
        )
    }

    override fun getUserInfo(accessToken: String): MemberAttributes {
        val kakaoUserInfoRequest =
            KakaoUserInfoRequest(secureResource = properties.secureResource, scope = properties.scope)
        val parameters = WebClientUtils.convertParameters(kakaoUserInfoRequest, objectMapper)
        val response = apiWebClient.post()
            .uri { uriBuilder -> uriBuilder.path(properties.userInfoUri).build() }
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(parameters))
            .retrieve().onStatus({ it -> it != HttpStatus.OK },
                {
                    it.createException().flatMap { err ->
                        logger.error(err.message)
                        throw IllegalStateException(err.message)
                    }
                })
            .bodyToMono(KakaoUserInfoResponse::class.java)
            .block()

        return MemberAttributes(
            name = response!!.kakaoAccount.name,
            email = response.kakaoAccount.email,
            image = response.kakaoAccount.profile.profileImageUrl,
            oauthId = response.id.toString()
        )
    }


    override fun getOAuthType(): OAuth2Type {
        return OAuth2Type.KAKAO
    }

    override fun getClient(): OAuth2WebClient {
        return this
    }
}