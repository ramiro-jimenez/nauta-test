package com.nauta.interviewexample.core.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class InvoiceTest {

    @Test
    fun `create invoice with all fields`() {
        val clientId = UUID.randomUUID()
        val bookingId = UUID.randomUUID()
        val orderId = UUID.randomUUID()
        val code = "INV123"
        val invoice = Invoice.create(clientId, bookingId, orderId, code)

        assertEquals(clientId, invoice.clientId)
        assertEquals(bookingId, invoice.bookingId)
        assertEquals(orderId, invoice.orderId)
        assertEquals(code, invoice.code)
        assertNotNull(invoice.id)
    }
}
