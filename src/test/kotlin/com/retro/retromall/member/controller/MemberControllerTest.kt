package com.retro.retromall.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.ApiDocumentUtils.Companion.getDocumentRequest
import com.retro.ApiDocumentUtils.Companion.getDocumentResponse
import com.retro.common.JwtTokenProvider
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.dto.MemberAttributes
import com.retro.retromall.member.dto.TokenAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.service.MemberService
import com.retro.retromall.oauth.client.dto.OAuthAuthorizationCode
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter


@ActiveProfiles("local")
@WebMvcTest(MemberController::class)
@ExtendWith(RestDocumentationExtension::class)
class MemberControllerTest {

    private lateinit var objectMapper : ObjectMapper

    @MockBean
    lateinit var memberService : MemberService

    @MockBean
    private lateinit var jwtTokenProvider : JwtTokenProvider

    private lateinit var mockMvc: MockMvc

    private lateinit var kakaoOauthLoginRequest : LinkedMultiValueMap<String, String>

    private lateinit var naverOauthLoginRequest : LinkedMultiValueMap<String, String>

    private lateinit var loginResponse: LoginResponse

    @BeforeEach
    fun setUp(webApplicationContext : WebApplicationContext,
              restDocumentationContextProvider : RestDocumentationContextProvider
    ){
        this.objectMapper = ObjectMapper()

        val filter = CharacterEncodingFilter("UTF-8",true)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(
                    restDocumentationContextProvider
                )
            )
            .addFilter<DefaultMockMvcBuilder?>(filter)
            .build()

        setLoginRequest()
        setLoginResponse()

        given(memberService.loginMemberWithOAuth(any(OAuthType::class.java),any(OAuthAuthorizationCode::class.java)))
            .willReturn(loginResponse)
    }

    private fun setLoginResponse(){
        val memberAttributes = MemberAttributes(1L,OAuthType.NAVER,"FXsYTEv0EWgPr8cJIJa6vkjmC-OT_fNqdd89cwWczdM","테스트닉네임","https://ssl.pstatic.net/static/pwe/address/img_profile.png",
            "USER","DELETE_PRODUCT, UPDATE_PRODUCT, CREATE_PRODUCT")

        val tokenAttributes = TokenAttributes("Bearer","accessToken", 1686149842378)

        loginResponse = LoginResponse("refreshToken",memberAttributes, tokenAttributes)
    }

    private fun setLoginRequest(){
        var naverLoginRequest = LinkedMultiValueMap<String,String>()
        naverLoginRequest.add("code","valid-authorization-code")
        naverLoginRequest.add("state","state")

        var kakaoLoginRequest = LinkedMultiValueMap<String,String>()
        kakaoLoginRequest.add("code","valid-authorization-code")

        naverOauthLoginRequest = naverLoginRequest
        kakaoOauthLoginRequest = kakaoLoginRequest
    }



    @Test
    @DisplayName("유효한 요청값에 대한 카카오 Oauth 로그인")
    fun loginKakaoWithValidRequest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/members/oauth/login/kakao")
                .params(kakaoOauthLoginRequest)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("테스트닉네임")))
            .andExpect(cookie().value("refresh-token","refreshToken"))
            .andDo(
                document(
                    "loginKakaoWithValidRequest",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    requestParameters(
                        parameterWithName("code").description("발급받은 authorizationCode")
                    ),
                    responseFields(
                        fieldWithPath("memberAttributes").type(JsonFieldType.OBJECT).description("로그인 유저 정보"),
                        fieldWithPath("memberAttributes.id").type(JsonFieldType.NUMBER).description("유저 ID (DB에 저장된 키값)"),
                        fieldWithPath("memberAttributes.oauthType").type(JsonFieldType.STRING).description("OAuth 타입"),
                        fieldWithPath("memberAttributes.oauthId").type(JsonFieldType.STRING).description("OAuth ID"),
                        fieldWithPath("memberAttributes.nickName").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("memberAttributes.profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                        fieldWithPath("memberAttributes.roles").type(JsonFieldType.STRING).description("역할"),
                        fieldWithPath("memberAttributes.permissions").type(JsonFieldType.STRING).description("권한"),
                        fieldWithPath("tokenAttributes").type(JsonFieldType.OBJECT).description("토큰 정보"),
                        fieldWithPath("tokenAttributes.grantType").type(JsonFieldType.STRING).description("grantType"),
                        fieldWithPath("tokenAttributes.accessToken").type(JsonFieldType.STRING).description("accessToken"),
                        fieldWithPath("tokenAttributes.expiredAccessToken").type(JsonFieldType.NUMBER).description("토큰 만료기간(UTC)"),
                    )
                )
            )


    }

    @Test
    @DisplayName("유효한 요청값에 대한 네이버 Oauth 로그인")
    fun loginNaverWithValidRequest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/members/oauth/login/naver")
                .params(naverOauthLoginRequest)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("테스트닉네임")))
            .andExpect(cookie().value("refresh-token","refreshToken"))
            .andDo(
                document(
                    "loginNaverWithValidRequest",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    requestParameters(
                        parameterWithName("code").description("발급받은 authorizationCode"),
                        parameterWithName("state").description("authorizationCode 발급 시 사용했던 state 값")
                    ),
                    responseFields(
                        fieldWithPath("memberAttributes").type(JsonFieldType.OBJECT).description("로그인 유저 정보"),
                        fieldWithPath("memberAttributes.id").type(JsonFieldType.NUMBER).description("유저 ID (DB에 저장된 키값)"),
                        fieldWithPath("memberAttributes.oauthType").type(JsonFieldType.STRING).description("OAuth 타입"),
                        fieldWithPath("memberAttributes.oauthId").type(JsonFieldType.STRING).description("OAuth ID"),
                        fieldWithPath("memberAttributes.nickName").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("memberAttributes.profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                        fieldWithPath("memberAttributes.roles").type(JsonFieldType.STRING).description("역할"),
                        fieldWithPath("memberAttributes.permissions").type(JsonFieldType.STRING).description("권한"),
                        fieldWithPath("tokenAttributes").type(JsonFieldType.OBJECT).description("토큰 정보"),
                        fieldWithPath("tokenAttributes.grantType").type(JsonFieldType.STRING).description("grantType"),
                        fieldWithPath("tokenAttributes.accessToken").type(JsonFieldType.STRING).description("accessToken"),
                        fieldWithPath("tokenAttributes.expiredAccessToken").type(JsonFieldType.NUMBER).description("토큰 만료기간(UTC)"),
                    )
                )
            )
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
}