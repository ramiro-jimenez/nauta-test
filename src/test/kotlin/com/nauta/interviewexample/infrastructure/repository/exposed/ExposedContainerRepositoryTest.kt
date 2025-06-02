package com.nauta.interviewexample.infrastructure.repository.exposed

import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.infrastructure.repository.exposed.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExposedContainerRepositoryTest {

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
    fun `can save and retrieve Container by clientId and bookingId`() {
        val clientId = UUID.randomUUID()
        val booking = Booking.create(clientId, "BOOK2")
        val container = Container.create("CONT-2")
        booking.addAllContainers(setOf(container))
        bookingRepository.saveAllChanges(booking)

        val byBooking = containerRepository.findByBookingId(booking.id)
        Assertions.assertTrue(byBooking.any { it.code == "CONT-2" })

        val byClient = containerRepository.findByClientId(clientId)
        Assertions.assertTrue(byClient.any { it.code == "CONT-2" })
    }
}
