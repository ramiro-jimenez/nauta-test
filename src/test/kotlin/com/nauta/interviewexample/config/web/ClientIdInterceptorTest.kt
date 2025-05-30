package com.nauta.interviewexample.config.web

import io.mockk.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.PrintWriter

class ClientIdInterceptorTest {

    private val interceptor = ClientIdInterceptor()

    @Test
    fun `preHandle allows request if X-Client-ID is present`() {
        val request = mockk<HttpServletRequest> {
            every { getHeader("X-Client-ID") } returns "test-client"
        }
        val response = mockk<HttpServletResponse>(relaxed = true)
        val handler = Any()

        val result = interceptor.preHandle(request, response, handler)

        assertTrue(result)
        verify(exactly = 0) { response.status = any() }
        verify(exactly = 0) { response.writer }
    }

    @Test
    fun `preHandle rejects request if X-Client-ID is missing`() {
        val request = mockk<HttpServletRequest> {
            every { getHeader("X-Client-ID") } returns null
        }
        val writer = mockk<PrintWriter>(relaxed = true)
        val response = mockk<HttpServletResponse>(relaxed = true)
        every { response.writer } returns writer
        val handler = Any()

        val result = interceptor.preHandle(request, response, handler)

        assertFalse(result)
        verify { response.status = HttpServletResponse.SC_UNAUTHORIZED }
        verify { response.contentType = "text/plain" }
        verify { writer.write("Missing X-Client-ID header") }
        verify { writer.flush() }
    }
}
