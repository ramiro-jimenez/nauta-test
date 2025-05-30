package com.nauta.interviewexample.config.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class ClientIdInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val clientId = request.getHeader("X-Client-ID")
        if (clientId.isNullOrBlank()) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "text/plain"
            response.writer.write("Missing X-Client-ID header")
            response.writer.flush()
            return false
        }
        return true
    }
}
