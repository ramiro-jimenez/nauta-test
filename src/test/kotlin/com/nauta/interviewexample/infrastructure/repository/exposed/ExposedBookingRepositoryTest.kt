package com.nauta.interviewexample.infrastructure.repository.exposed

import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.infrastructure.repository.exposed.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExposedBookingRepositoryTest {

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
    fun `can save and retrieve Booking with containers and orders`() {
        val clientId = UUID.randomUUID()
        val bookingCode = "BOOK-TEST"
        val booking = Booking.create(clientId, bookingCode)
        val container = Container.create("CONT-1")
        val order = Order.create(clientId, booking.id, "PURCH-1", listOf("INV-1"))
        booking.addAllContainers(setOf(container))
        booking.addAllOrders(setOf(order))

        bookingRepository.saveAllChanges(booking)

        val loaded = bookingRepository.findByCode(bookingCode, clientId)
        Assertions.assertNotNull(loaded)
        Assertions.assertEquals(bookingCode, loaded!!.code)
        Assertions.assertEquals(1, loaded.containers.size)
        Assertions.assertEquals(1, loaded.orders.size)
        Assertions.assertEquals(container.code, loaded.containers.first().code)
        Assertions.assertEquals(order.purchase, loaded.orders.first().purchase)
    }
}
