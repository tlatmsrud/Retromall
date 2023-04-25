package com.retro.retromall.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.retro.retromall.member.controller.MemberController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc

@ExtendWith(SpringExtension::class)
@WebMvcTest(MemberController::class)
class MemberControllerMockMvcTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var jsonTester: JacksonTester<Any>

    @BeforeEach
    fun setup() {
        JacksonTester.initFields(this, ObjectMapper())
    }


}