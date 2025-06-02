package com.nauta.interviewexample.core.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class BookingTest {

    @Test
    fun `create booking and add containers and orders`() {
        val clientId = UUID.randomUUID()
        val code = "BOOK123"
        val booking = Booking.create(clientId, code)

        assertEquals(clientId, booking.clientId)
        assertEquals(code, booking.code)
        assertTrue(booking.containers.isEmpty())
        assertTrue(booking.orders.isEmpty())

        val container1 = Container.create("CONT1")
        val container2 = Container.create("CONT2")
        booking.addAllContainers(setOf(container1, container2))
        assertEquals(2, booking.containers.size)
        assertTrue(booking.containers.contains(container1))
        assertTrue(booking.containers.contains(container2))

        val order1 = Order.create(clientId, booking.id, "PURCH1", listOf("INV1"))
        val order2 = Order.create(clientId, booking.id, "PURCH2", listOf("INV2"))
        booking.addAllOrders(setOf(order1, order2))
        assertEquals(2, booking.orders.size)
        assertTrue(booking.orders.contains(order1))
        assertTrue(booking.orders.contains(order2))
    }

    @Test
    fun `create booking with containers and orders in create method`() {
        val clientId = UUID.randomUUID()
        val code = "BOOK456"
        val container1 = Container.create("CONT10")
        val container2 = Container.create("CONT20")
        val order1 = Order.create(clientId, UUID.randomUUID(), "PURCH10", listOf("INV10"))
        val order2 = Order.create(clientId, UUID.randomUUID(), "PURCH20", listOf("INV20"))

        val containers = mutableSetOf(container1, container2)
        val orders = mutableSetOf(order1, order2)

        val booking = Booking.create(clientId, code, containers, orders)

        assertEquals(clientId, booking.clientId)
        assertEquals(code, booking.code)
        assertEquals(containers, booking.containers)
        assertEquals(orders, booking.orders)
    }
}
