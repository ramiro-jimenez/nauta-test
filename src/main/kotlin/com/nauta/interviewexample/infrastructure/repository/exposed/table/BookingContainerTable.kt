package com.nauta.interviewexample.infrastructure.repository.exposed.table

import org.jetbrains.exposed.sql.Table

object BookingContainerTable: Table(
    name = "booking_container"
) {
    val booking = reference("booking_id", BookingTable).index()
    val container = reference("container_id", ContainerTable).index()
    override val primaryKey = PrimaryKey(booking, container, name = "PK_booking_container")
}