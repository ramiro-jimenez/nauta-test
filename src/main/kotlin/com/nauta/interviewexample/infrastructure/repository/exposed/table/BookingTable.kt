package com.nauta.interviewexample.infrastructure.repository.exposed.table

import org.jetbrains.exposed.dao.id.UUIDTable

object BookingTable: UUIDTable(
    name = "booking",
    columnName = "id"
) {
    val clientId = uuid("client_id").index()
    val code = varchar("code", 50).uniqueIndex()
}