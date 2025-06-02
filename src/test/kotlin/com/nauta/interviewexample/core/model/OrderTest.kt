package com.nauta.interviewexample.core.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class OrderTest {

    @Test
    fun `create order with invoices and add invoices`() {
        val clientId = UUID.randomUUID()
        val bookingId = UUID.randomUUID()
        val purchase = "PURCH123"
        val invoiceCodes = listOf("INV1", "INV2")
        val order = Order.create(clientId, bookingId, purchase, invoiceCodes)

        assertEquals(clientId, order.clientId)
        assertEquals(bookingId, order.bookingId)
        assertEquals(purchase, order.purchase)
        assertEquals(2, order.invoices.size)
        assertTrue(order.invoices.all { it.orderId == order.id })

        val invoice3 = Invoice.create(clientId, bookingId, order.id, "INV3")
        order.addInvoice(invoice3)
        assertTrue(order.invoices.contains(invoice3))

        val invoice4 = Invoice.create(clientId, bookingId, order.id, "INV4")
        val invoiceWrongOrder = Invoice.create(clientId, bookingId, UUID.randomUUID(), "INV5")
        order.addInvoices(setOf(invoice4, invoiceWrongOrder))
        assertTrue(order.invoices.contains(invoice4))
        assertFalse(order.invoices.contains(invoiceWrongOrder))
    }
}
