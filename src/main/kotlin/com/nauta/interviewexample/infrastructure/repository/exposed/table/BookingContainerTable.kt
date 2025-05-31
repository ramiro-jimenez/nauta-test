package com.nauta.interviewexample.infrastructure.repository.exposed.table

import org.jetbrains.exposed.dao.id.UUIDTable

object BookingContainerTable: UUIDTable(
    name = "booking_container",
    columnName = "id"
) {
    val booking = reference("booking_id", BookingTable).index()
    val container = reference("container_id", ContainerTable).index()
}