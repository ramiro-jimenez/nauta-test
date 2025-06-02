package com.nauta.interviewexample.controller

import com.nauta.interviewexample.core.action.EmailRegisterAction
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@ExtendWith(MockKExtension::class)
@WebMvcTest(EmailController::class)
class EmailControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    @TestConfiguration
    class MockConfig {
        @Bean
        fun emailRegisterAction(): EmailRegisterAction = mockk(relaxed = true)
    }

    @Autowired
    lateinit var emailRegisterAction: EmailRegisterAction

    @Test
    fun `should return 200 if request is valid`() {
        every { emailRegisterAction.invoke(any()) } returns Unit

        val body = """
            {
              "booking": "BOOK123",
              "containers": [{"container": "CONT1"}],
              "orders": [{"purchase": "PUR1", "invoices": [{"invoice": "INV1"}]}]
            }
        """.trimIndent()

        callApiEmail(body)
            .andExpect { status { isOk() } }
            .andExpect { content { string(org.hamcrest.Matchers.containsString("Email processed successfully")) } }
        verify { emailRegisterAction.invoke(any()) }
    }

    @Test
    fun `should return 400 if booking code is missing`() {
        val body = """
            {
              "booking": "",
              "containers": [{"container": "CONT1"}],
              "orders": [{"purchase": "PUR1", "invoices": [{"invoice": "INV1"}]}]
            }
        """.trimIndent()
        callApiEmail(body)
            .andExpect { status { isBadRequest() } }
            .andExpect { content { string(org.hamcrest.Matchers.containsString("booking")) } }
    }

    @Test
    fun `should return 400 if container code is missing`() {
        val body = """
            {
              "booking": "BOOK123",
              "containers": [{"container": ""}],
              "orders": [{"purchase": "PUR1", "invoices": [{"invoice": "INV1"}]}]
            }
        """.trimIndent()
        mockMvc.post("/api/email") {
            contentType = MediaType.APPLICATION_JSON
            content = body
            header("X-Client-ID", "123e4567-e89b-12d3-a456-426614174000")
        }
            .andExpect { status { isBadRequest() } }
            .andExpect { content { string(org.hamcrest.Matchers.containsString("containers[0].container")) } }
    }

    @Test
    fun `should return 400 if purchase code is missing in order`() {
        val body = """
            {
              "booking": "BOOK123",
              "containers": [{"container": "CONT1"}],
              "orders": [{"purchase": "", "invoices": [{"invoice": "INV1"}]}]
            }
        """.trimIndent()
        mockMvc.post("/api/email") {
            contentType = MediaType.APPLICATION_JSON
            content = body
            header("X-Client-ID", "123e4567-e89b-12d3-a456-426614174000")
        }
            .andExpect { status { isBadRequest() } }
            .andExpect { content { string(org.hamcrest.Matchers.containsString("orders[0].purchase")) } }
    }

    @Test
    fun `should return 400 if invoice code is missing in order`() {
        val body = """
            {
              "booking": "BOOK123",
              "containers": [{"container": "CONT1"}],
              "orders": [{"purchase": "PUR1", "invoices": [{"invoice": ""}]}]
            }
        """.trimIndent()
        mockMvc.post("/api/email") {
            contentType = MediaType.APPLICATION_JSON
            content = body
            header("X-Client-ID", "123e4567-e89b-12d3-a456-426614174000")
        }
            .andExpect { status { isBadRequest() } }
            .andExpect { content { string(org.hamcrest.Matchers.containsString("orders[0].invoices[0].invoice")) } }
    }

    private fun callApiEmail(body: String) =
        mockMvc.post("/api/email") {
            contentType = MediaType.APPLICATION_JSON
            content = body
            header("X-Client-ID", "123e4567-e89b-12d3-a456-426614174000")
        }
}

