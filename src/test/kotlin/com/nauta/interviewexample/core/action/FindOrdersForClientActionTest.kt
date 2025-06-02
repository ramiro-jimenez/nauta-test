package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.model.Invoice
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.repository.OrderRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class FindOrdersForClientActionTest {

    private val orderRepository = mockk<OrderRepository>()
    private val action = FindOrdersForClientAction(orderRepository)

    @Test
    fun `should return orders for client and call repository`() {
        val clientId = UUID.randomUUID()
        val orders = setOf(
            orderFor(PURCHASE_1, listOf(INVOICE_1)),
            orderFor(PURCHASE_2, listOf(INVOICE_2))
        )
        every { orderRepository.findByClientId(clientId) } returns orders

        val result = action.invoke(clientId)

        assertEquals(orders, result)
        verify(exactly = 1) { orderRepository.findByClientId(clientId) }
    }

    private fun orderFor(purchase: String, invoiceCodes: List<String>) =
        Order.create(CLIENT_ID, BOOKING_ID, purchase, invoiceCodes)

    private companion object {
        val CLIENT_ID: UUID = UUID.randomUUID()
        val BOOKING_ID: UUID = UUID.randomUUID()
        const val PURCHASE_1 = "PURCHASE-1"
        const val PURCHASE_2 = "PURCHASE-2"
        const val INVOICE_1 = "INV-1"
        const val INVOICE_2 = "INV-2"
    }
}
