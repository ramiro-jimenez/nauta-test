package com.nauta.interviewexample.core.repository

import com.nauta.interviewexample.core.model.Invoice
import java.util.*

interface InvoiceRepository {
    fun saveAll(invoices: List<Invoice>)
    fun findByBookingId(bookingId: UUID): Set<Invoice>
    fun findByClientId(clientId: UUID): Set<Invoice>
    fun findByOrderIds(orderIds: Set<UUID>): Set<Invoice>

}
