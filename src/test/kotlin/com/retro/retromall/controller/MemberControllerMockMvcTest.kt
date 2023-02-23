package com.retro.retromall.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.retromall.member.dto.LoginResponse
import com.retro.retromall.member.controller.MemberController
import com.retro.retromall.member.dto.LoginRequest
import com.retro.retromall.member.dto.TokenAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.service.MemberWriteService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@ExtendWith(SpringExtension::class)
@WebMvcTest(MemberController::class)
class MemberControllerMockMvcTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var memberWriteService: MemberWriteService

    private lateinit var jsonTester: JacksonTester<Any>

    @BeforeEach
    fun setup() {
        JacksonTester.initFields(this, ObjectMapper())
    }

    @Test
    fun canRetrieveByIdWhenExist() {
        val tokenAttributes = TokenAttributes(grantType = "Bearer", accessToken = "access", refreshToken = "refresh")
        val loginResponse = LoginResponse("nickName", tokenAttributes)

        //given
        given(memberWriteService.findMemberByOauth(OAuthType.KAKAO, "Password")).willReturn(loginResponse)

        //when
        val loginRequest = LoginRequest(OAuthType.KAKAO, "Password")
        val response = mockMvc
            .perform(post("/members/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTester.write(loginRequest).json))
            .andReturn().response

        assertEquals(response.status, HttpStatus.OK.value())
        assertEquals(response.contentAsString, jsonTester.write(tokenAttributes).json)
    }
}