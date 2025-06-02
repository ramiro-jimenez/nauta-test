package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.repository.OrderRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class FindOrdersForContainerIdActionTest {

    private val orderRepository = mockk<OrderRepository>()
    private val action = FindOrdersForContainerIdAction(orderRepository)

    @Test
    fun `should return orders for containerId and clientId and call repository`() {
        val containerId = "container-xyz"
        val clientId = UUID.randomUUID()
        val orders = setOf(
            mockk<Order>()
        )
        every { orderRepository.findByContainerId(containerId, clientId) } returns orders

        val result = action.invoke(containerId, clientId)

        assertEquals(orders, result)
        verify(exactly = 1) { orderRepository.findByContainerId(containerId, clientId) }
    }
}
