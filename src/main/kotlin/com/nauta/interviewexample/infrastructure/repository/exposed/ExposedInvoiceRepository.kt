package com.nauta.interviewexample.infrastructure.repository.exposed

import com.nauta.interviewexample.core.model.Invoice
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.repository.InvoiceRepository
import com.nauta.interviewexample.infrastructure.repository.exposed.table.InvoiceTable
import com.nauta.interviewexample.infrastructure.repository.exposed.table.OrderTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ExposedInvoiceRepository(
    private val database: Database
) : InvoiceRepository {

    override fun findByBookingId(bookingId: UUID): Set<Invoice> {
        return transaction(database) {
            InvoiceTable.selectAll()
                .where { InvoiceTable.booking eq bookingId }
                .map { row -> toInvoice(row) }.toSet()
        }
    }

    override fun findByClientId(clientId: UUID): Set<Invoice> {
        return transaction(database) {
            InvoiceTable.selectAll()
                .where { InvoiceTable.clientId eq clientId }
                .map { row -> toInvoice(row) }.toSet()
        }
    }

    override fun findByOrderId(orderId: UUID): Set<Invoice> {
        return transaction(database) {
            InvoiceTable.selectAll()
                .where { InvoiceTable.order eq orderId }
                .map { row -> toInvoice(row) }.toSet()
        }
    }

    override fun saveAll(invoices: List<Invoice>) {
        transaction(database) {
            InvoiceTable.batchUpsert(invoices, shouldReturnGeneratedValues = false) { invoice ->
                this[InvoiceTable.id] = invoice.id
                this[InvoiceTable.clientId] = invoice.clientId
                this[InvoiceTable.booking] = invoice.bookingId
                this[InvoiceTable.order] = invoice.orderId
                this[InvoiceTable.code] = invoice.code
            }
        }
    }

    private fun toInvoice(row: ResultRow) = Invoice(
        id = row[InvoiceTable.id].value,
        clientId = row[InvoiceTable.clientId],
        orderId = row[InvoiceTable.order].value,
        bookingId = row[InvoiceTable.booking].value,
        code = row[InvoiceTable.code],
    )
}