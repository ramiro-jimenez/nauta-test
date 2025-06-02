package com.nauta.interviewexample.infrastructure.repository.exposed

import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.repository.BookingRepository
import com.nauta.interviewexample.core.repository.ContainerRepository
import com.nauta.interviewexample.core.repository.OrderRepository
import com.nauta.interviewexample.infrastructure.repository.exposed.table.BookingTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ExposedBookingRepository(
    private val database: Database,
    private val containerRepository: ContainerRepository,
    private val orderRepository: OrderRepository
): BookingRepository {

    override fun findByCode(code: String, clientId: UUID): Booking? {
        return transaction(database) {
            val booking = BookingTable.selectAll()
                .where { (BookingTable.code eq code) and
                        (BookingTable.clientId eq clientId) }
                .map { row -> toBooking(row) }
                .singleOrNull()

            return@transaction booking?.apply {
                addAllContainers(containerRepository.findByBookingId(id))
                addAllOrders(orderRepository.findByBookingId(id))
            }
        }
    }

    override fun saveAllChanges(booking: Booking) {
        transaction(database) {
            BookingTable.upsert {
                it[id] = booking.id
                it[code] = booking.code
                it[clientId] = booking.clientId
            }
            containerRepository.saveAllForBooking(booking.id, booking.containers)
            orderRepository.saveAllForBooking(booking.orders)
        }
    }

    private fun toBooking(row: ResultRow) =
        Booking(
            id = row[BookingTable.id].value,
            code = row[BookingTable.code],
            clientId = row[BookingTable.clientId]
        )

}
