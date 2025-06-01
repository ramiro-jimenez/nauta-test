package com.nauta.interviewexample.infrastructure.repository.exposed

import com.nauta.interviewexample.core.model.Invoice
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.repository.InvoiceRepository
import com.nauta.interviewexample.core.repository.OrderRepository
import com.nauta.interviewexample.infrastructure.repository.exposed.table.OrderTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ExposedOrderRepository(
    private val database: Database,
    private val invoiceRepository: InvoiceRepository
) : OrderRepository {

    override fun findByBookingId(bookingId: UUID): Set<Order> {
        val invoices = invoiceRepository.findByBookingId(bookingId)
        return transaction(database) {
            OrderTable.selectAll()
                .where { OrderTable.booking eq bookingId }
                .map { row -> toOrder(row, invoices) }.toSet()

        }
    }

    override fun saveAllForBooking(orders: Set<Order>) {
        transaction(database) {
            saveNotExistingOrders(orders)
        }
    }

    private fun saveNotExistingOrders(orders: Set<Order>) {
        transaction(database) {
            val existingOrders = OrderTable.selectAll()
                .where { OrderTable.id inList orders.map { it.id } }
                .map { it[OrderTable.id].value }

            val newOrders = orders.filterNot { it.id in existingOrders }.toSet()

            if (newOrders.isNotEmpty()) {
                saveAll(newOrders)
                invoiceRepository.saveAll(newOrders.flatMap { it.invoices })
            }
        }
    }

    private fun saveAll(orders: Set<Order>) {
        transaction(database) {
            OrderTable.batchUpsert(orders, shouldReturnGeneratedValues = false) { order ->
                this[OrderTable.id] = order.id
                this[OrderTable.clientId] = order.clientId
                this[OrderTable.booking] = order.bookingId
                this[OrderTable.purchase] = order.purchase
            }
        }
    }

    private fun toOrder(
        row: ResultRow,
        invoices: Set<Invoice>
    ) = Order(
        id = row[OrderTable.id].value,
        clientId = row[OrderTable.clientId],
        bookingId = row[OrderTable.booking].value,
        purchase = row[OrderTable.purchase],
        invoices = invoices.filter { it.orderId == row[OrderTable.id].value }.toMutableSet()
    )
}