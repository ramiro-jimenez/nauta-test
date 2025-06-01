package com.nauta.interviewexample.core.model

import com.nauta.interviewexample.util.UUIDExtensions.fromSeed
import java.util.*

data class Order internal constructor(
    val id: UUID,
    val clientId: UUID,
    val bookingId: UUID,
    val purchase: String,
    val invoices: MutableSet<Invoice> = mutableSetOf()
) {

    fun addInvoice(invoice: Invoice) {
        invoices.add(invoice)
    }

    fun addAllInvoices(value: Set<Invoice>) {
        invoices.addAll(value)
    }

    companion object {
        fun create(
            clientId: UUID,
            bookingId: UUID,
            purchase: String,
            invoices: MutableSet<Invoice> = mutableSetOf()
        ): Order {
            return Order(
                id = fromSeed(clientId.toString() + bookingId.toString() + purchase),
                clientId = clientId,
                bookingId = bookingId,
                purchase = purchase,
                invoices = invoices
            )
        }
    }
}