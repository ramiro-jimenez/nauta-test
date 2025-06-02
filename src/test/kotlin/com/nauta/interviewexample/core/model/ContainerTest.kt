package com.nauta.interviewexample.core.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ContainerTest {

    @Test
    fun `create container with code`() {
        val code = "CONT123"
        val container = Container.create(code)

        assertEquals(code, container.code)
        assertNotNull(container.id)
    }
}
