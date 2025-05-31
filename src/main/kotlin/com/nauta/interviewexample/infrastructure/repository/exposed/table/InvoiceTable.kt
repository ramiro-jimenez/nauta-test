package com.nauta.interviewexample.infrastructure.repository.exposed.table

import org.jetbrains.exposed.dao.id.UUIDTable

object InvoiceTable: UUIDTable(
    name = "invoice",
    columnName = "id"
) {
    val clientId = uuid("client_id").index()
    val booking = reference("booking_id", BookingTable).index()
    val order = reference("order_id", OrderTable).index()
    val purchase = varchar("purchase", 50).uniqueIndex()
}