package com.nauta.interviewexample.controller

import com.nauta.interviewexample.core.action.FindContainersForPurchaseIdAction
import com.nauta.interviewexample.core.action.FindOrdersForClientAction
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
@WebMvcTest(OrderController::class)
class OrderControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    @TestConfiguration
    class MockConfig {
        @Bean
        fun findOrdersForClientAction(): FindOrdersForClientAction = mockk(relaxed = true)

        @Bean
        fun findContainersForPurchaseIdAction(): FindContainersForPurchaseIdAction = mockk(relaxed = true)
    }

    @Autowired
    lateinit var findOrdersForClientAction: FindOrdersForClientAction

    @Autowired
    lateinit var findContainersForPurchaseIdAction: FindContainersForPurchaseIdAction

    private val clientId = "123e4567-e89b-12d3-a456-426614174000"

    @Test
    fun `should return orders for client`() {
        val orders = setOf(Order.create(UUID.randomUUID(), UUID.randomUUID(), "PUR1"))
        every { findOrdersForClientAction.invoke(UUID.fromString(clientId)) } returns orders

        mockMvc.get("/api/orders") {
            header("X-Client-ID", clientId)
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
        verify { findOrdersForClientAction.invoke(UUID.fromString(clientId)) }
    }

    @Test
    fun `should return containers for purchase`() {
        val containers = setOf(Container.create("CONT1"), Container.create("CONT2"))
        every { findContainersForPurchaseIdAction.invoke("PUR1", UUID.fromString(clientId)) } returns containers

        mockMvc.get("/api/orders/PUR1/containers") {
            header("X-Client-ID", clientId)
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
        verify { findContainersForPurchaseIdAction.invoke("PUR1", UUID.fromString(clientId)) }
    }
}