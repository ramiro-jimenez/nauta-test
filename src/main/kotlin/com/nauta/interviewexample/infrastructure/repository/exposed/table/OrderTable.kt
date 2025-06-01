package com.nauta.interviewexample.infrastructure.repository.exposed.table

import org.jetbrains.exposed.dao.id.UUIDTable

object OrderTable: UUIDTable(
    name = "bk_order",
    columnName = "id"
) {
    val clientId = uuid("client_id").index()
    val booking = reference("booking_id", BookingTable).index()
    val purchase = varchar("purchase", 50).uniqueIndex()
}