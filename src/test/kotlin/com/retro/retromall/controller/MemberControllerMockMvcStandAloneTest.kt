package com.retro.retromall.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.retromall.common.TokenInfo
import com.retro.retromall.member.controller.MemberController
import com.retro.retromall.member.dto.LoginRequest
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
        val loginRequest = LoginRequest(memberId = "TestId", password = "Password")
        val tokenInfo = TokenInfo("Bearer", "access", "refresh")
        every { memberService.login("TestId", "Password") } returns tokenInfo
        every { memberController.login(loginRequest) } returns ResponseEntity.ok(tokenInfo)

        //when
        val response = mvc.perform(
            post("/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(loginRequest).json).accept(MediaType.APPLICATION_JSON)
        )
            .andReturn().response

        //then
        assertEquals(response.status, HttpStatus.OK.value())
        assertEquals(
            response.contentAsString, jacksonTester.write(
                TokenInfo("Bearer", "access", "refresh")
            ).json
        )
    }

    @Test
    fun canRetrieveByIdWhenDoesNotExist() {
        //given
        every { memberService.login("TestId", "Password") }.throws(EntityNotFoundException("Entity Not Found"))

        //when
        val loginRequest = LoginRequest(memberId = "TestId", password = "Password")
        val response = mvc.perform(
            post("/members/login").contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(loginRequest).json)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andReturn().response

        //then
        assertEquals(response.status, HttpStatus.BAD_REQUEST.value())
        assertEquals(response.contentAsString, null)
    }
}