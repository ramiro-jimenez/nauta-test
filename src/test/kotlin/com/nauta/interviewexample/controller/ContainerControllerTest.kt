package com.nauta.interviewexample.controller

import com.nauta.interviewexample.core.action.FindContainersForClientAction
import com.nauta.interviewexample.core.action.FindOrdersForContainerIdAction
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.model.Order
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
import org.springframework.test.web.servlet.get

import java.util.*

@ExtendWith(MockKExtension::class)
@WebMvcTest(ContainerController::class)
class ContainerControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    @TestConfiguration
    class MockConfig {
        @Bean
        fun findContainersForClientAction(): FindContainersForClientAction = mockk(relaxed = true)

        @Bean
        fun findOrdersForContainerIdAction(): FindOrdersForContainerIdAction = mockk(relaxed = true)
    }

    @Autowired
    lateinit var findContainersForClientAction: FindContainersForClientAction

    @Autowired
    lateinit var findOrdersForContainerIdAction: FindOrdersForContainerIdAction

    private val clientId = "123e4567-e89b-12d3-a456-426614174000"

    @Test
    fun `should return containers for client`() {
        val containers = setOf(Container.create("CONT1"), Container.create("CONT2"))
        every { findContainersForClientAction.invoke(UUID.fromString(clientId)) } returns containers

        mockMvc.get("/api/containers") {
            header("X-Client-ID", clientId)
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
        verify { findContainersForClientAction.invoke(UUID.fromString(clientId)) }
    }

    @Test
    fun `should return orders for container`() {
        val orders = setOf(Order.create(UUID.randomUUID(), UUID.randomUUID(), "PUR1"))
        every { findOrdersForContainerIdAction.invoke("CONT1", UUID.fromString(clientId)) } returns orders

        mockMvc.get("/api/containers/CONT1/orders") {
            header("X-Client-ID", clientId)
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
        verify { findOrdersForContainerIdAction.invoke("CONT1", UUID.fromString(clientId)) }
    }
}