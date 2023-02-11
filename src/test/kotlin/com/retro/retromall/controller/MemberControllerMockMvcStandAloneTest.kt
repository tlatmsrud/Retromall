package com.retro.retromall.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.retromall.member.dto.TokenAttributes
import com.retro.retromall.member.controller.MemberController
import com.retro.retromall.member.dto.LoginAttributes
import com.retro.retromall.member.enums.OAuthType
import com.retro.retromall.member.service.MemberService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import javax.persistence.EntityNotFoundException

@ExtendWith(MockitoExtension::class)
class MemberControllerMockMvcStandAloneTest {
    private var memberService: MemberService = mockk()
    private var memberController: MemberController = mockk()

    private val mvc: MockMvc = MockMvcBuilders.standaloneSetup(memberController)
        .build()
    private lateinit var jacksonTester: JacksonTester<Any>


    @BeforeEach
    fun init() {
        JacksonTester.initFields(this, ObjectMapper())
    }

    @Test
    fun canRetrieveByIdWhenExists() {
        //given
        val loginAttributes = LoginAttributes(oAuthType = OAuthType.KAKAO, authorizationCode = "Password")
        val tokenAttributes = TokenAttributes("Bearer", "access", "refresh")
        every { memberService.findMemberByOauth(OAuthType.KAKAO, "Password") } returns tokenAttributes
        every { memberController.login(loginAttributes) } returns ResponseEntity.ok(tokenAttributes)

        //when
        val response = mvc.perform(
            post("/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(loginAttributes).json)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().response

        //then
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(
            jacksonTester.write(
                TokenAttributes("Bearer", "access", "refresh")
            ).json, response.contentAsString
        )
    }

    @Test
    fun canRetrieveByIdWhenDoesNotExist() {
        //given
        every {
            memberService.findMemberByOauth(
                OAuthType.KAKAO,
                "Password"
            )
        }.throws(EntityNotFoundException("Entity Not Found"))

        //when
        val loginAttributes = LoginAttributes(OAuthType.KAKAO, "Password")
        val response = mvc.perform(
            post("/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(loginAttributes).json)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().response

        //then
        assertEquals(response.status, HttpStatus.BAD_REQUEST.value())
        assertEquals(response.contentAsString, "")
    }
}