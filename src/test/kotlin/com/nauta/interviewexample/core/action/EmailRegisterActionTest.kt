package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.repository.BookingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class EmailRegisterActionTest {
    private lateinit var bookingRepository: BookingRepository
    private lateinit var emailRegisterAction: EmailRegisterAction

    @BeforeEach
    fun setUp() {
        bookingRepository = mockk(relaxed = true)
        emailRegisterAction = EmailRegisterAction(bookingRepository)
    }

    @Test
    fun `should create new booking and save with containers and orders if booking does not exist`() {
        val clientId = UUID.randomUUID()
        val bookingCode = "BOOK-1"
        val containers = listOf("CONT-1", "CONT-2")
        val orders = listOf(
            EmailRegisterAction.OrderData(
                purchase = "PUR-1",
                invoices = listOf("INV-1", "INV-2")
            )
        )
        val actionData = EmailRegisterAction.ActionData(
            clientId = clientId,
            booking = bookingCode,
            containers = containers,
            orders = orders
        )
        every { bookingRepository.findByCode(bookingCode, clientId) } returns null
        val slot = slot<Booking>()
        every { bookingRepository.saveAllChanges(capture(slot)) } returns Unit

        emailRegisterAction.invoke(actionData)

        verify(exactly = 1) { bookingRepository.saveAllChanges(any()) }
        val savedBooking = slot.captured
        assertEquals(clientId, savedBooking.clientId)
        assertEquals(bookingCode, savedBooking.code)
        assertEquals(containers.toSet(), savedBooking.containers.map { it.code }.toSet())
        assertEquals(orders[0].purchase, savedBooking.orders.first().purchase)
        assertEquals(orders[0].invoices.toSet(), savedBooking.orders.first().invoices.map { it.code }.toSet())
    }

    @Test
    fun `should add only new containers and orders to existing booking`() {
        val clientId = UUID.randomUUID()
        val bookingCode = "BOOK-2"
        val existingContainer = Container.create("CONT-1")
        val existingOrder = Order.create(clientId, UUID.randomUUID(), "PUR-1", listOf("INV-1"))
        val existingBooking = Booking.create(clientId, bookingCode)
        existingBooking.addAllContainers(setOf(existingContainer))
        existingBooking.addAllOrders(setOf(existingOrder))

        val containers = listOf("CONT-1", "CONT-2") // CONT-2 is new
        val orders = listOf(
            EmailRegisterAction.OrderData(
                purchase = "PUR-1", // already exists
                invoices = listOf("INV-1")
            ),
            EmailRegisterAction.OrderData(
                purchase = "PUR-2", // new order
                invoices = listOf("INV-2")
            )
        )
        val actionData = EmailRegisterAction.ActionData(
            clientId = clientId,
            booking = bookingCode,
            containers = containers,
            orders = orders
        )
        every { bookingRepository.findByCode(bookingCode, clientId) } returns existingBooking
        val slot = slot<Booking>()
        every { bookingRepository.saveAllChanges(capture(slot)) } returns Unit

        emailRegisterAction.invoke(actionData)

        verify(exactly = 1) { bookingRepository.saveAllChanges(any()) }
        val savedBooking = slot.captured
        // Should have both containers
        assertEquals(setOf("CONT-1", "CONT-2"), savedBooking.containers.map { it.code }.toSet())
        // Should have both orders
        assertEquals(setOf("PUR-1", "PUR-2"), savedBooking.orders.map { it.purchase }.toSet())
    }
}

