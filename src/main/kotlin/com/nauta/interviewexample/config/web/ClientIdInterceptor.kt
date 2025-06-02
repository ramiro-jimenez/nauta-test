package com.nauta.interviewexample.config.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*

class ClientIdInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val uri = request.requestURI
        if (uri.startsWith("/api")) {
            return validateClientId(request, response)
        }
        return true
    }

    private fun validateClientId(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Boolean {
        val clientId = request.getHeader("X-Client-ID")
        if (clientId.isNullOrBlank()) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            response.contentType = "text/plain"
            response.writer.write("Missing X-Client-ID header")
            response.writer.flush()
            return false
        }
        try {
            UUID.fromString(clientId)
        } catch (e: IllegalArgumentException) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            response.contentType = "text/plain"
            response.writer.write("X-Client-ID header must be a valid UUID")
            response.writer.flush()
            return false
        }
        return true
    }
}
