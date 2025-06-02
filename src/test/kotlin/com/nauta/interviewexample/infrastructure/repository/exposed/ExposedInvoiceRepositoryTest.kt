package com.nauta.interviewexample.infrastructure.repository.exposed

import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.infrastructure.repository.exposed.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExposedInvoiceRepositoryTest {

    private lateinit var db: Database
    private lateinit var bookingRepository: ExposedBookingRepository
    private lateinit var containerRepository: ExposedContainerRepository
    private lateinit var orderRepository: ExposedOrderRepository
    private lateinit var invoiceRepository: ExposedInvoiceRepository

    @BeforeAll
    fun setupDb() {
        db = ExposedRepositoryMother.database()
        invoiceRepository = ExposedInvoiceRepository(db)
        orderRepository = ExposedOrderRepository(db, invoiceRepository)
        containerRepository = ExposedContainerRepository(db)
        bookingRepository = ExposedBookingRepository(db, containerRepository, orderRepository)
    }

    @AfterAll
    fun dropDb() {
        ExposedRepositoryMother.drop(db)
    }

    @Test
    fun `can save and retrieve Invoice by bookingId and clientId`() {
        val clientId = UUID.randomUUID()
        val booking = Booking.create(clientId, "BOOK4")
        val order = Order.create(clientId, booking.id, "PURCH-4", listOf("INV-4"))
        booking.addAllOrders(setOf(order))
        bookingRepository.saveAllChanges(booking)

        val byBooking = invoiceRepository.findByBookingId(booking.id)
        Assertions.assertTrue(byBooking.any { it.code == "INV-4" })

        val byClient = invoiceRepository.findByClientId(clientId)
        Assertions.assertTrue(byClient.any { it.code == "INV-4" })
    }
}
