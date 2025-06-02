package com.nauta.interviewexample.config.web

import io.mockk.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import java.util.*

class ClientIdInterceptorTest {

    private val interceptor = ClientIdInterceptor()

    @Test
    fun `preHandle allows request if X-Client-ID is present and URI starts with api`() {
        val request = mockk<HttpServletRequest> {
            every { getHeader("X-Client-ID") } returns UUID.randomUUID().toString()
            every { requestURI } returns "/api/test"
        }
        val response = mockk<HttpServletResponse>(relaxed = true)
        val handler = Any()

        val result = interceptor.preHandle(request, response, handler)

        assertTrue(result)
        verify(exactly = 0) { response.status = any() }
        verify(exactly = 0) { response.writer }
    }

    @Test
    fun `preHandle allows request if URI does not start with api`() {
        val request = mockk<HttpServletRequest> {
            every { requestURI } returns "/public"
        }
        val response = mockk<HttpServletResponse>(relaxed = true)
        val handler = Any()

        val result = interceptor.preHandle(request, response, handler)

        assertTrue(result)
        verify(exactly = 0) { response.status = any() }
        verify(exactly = 0) { response.writer }
    }

    @Test
    fun `preHandle rejects request if X-Client-ID is missing and URI starts with api`() {
        val request = mockk<HttpServletRequest> {
            every { getHeader("X-Client-ID") } returns null
            every { requestURI } returns "/api/test"
        }
        val writer = mockk<PrintWriter>(relaxed = true)
        val response = mockk<HttpServletResponse>(relaxed = true)
        every { response.writer } returns writer
        val handler = Any()

        val result = interceptor.preHandle(request, response, handler)

        assertFalse(result)
        verify { response.status = HttpServletResponse.SC_BAD_REQUEST }
        verify { response.contentType = "text/plain" }
        verify { writer.write("Missing X-Client-ID header") }
        verify { writer.flush() }
    }
}
