package com.nauta.interviewexample.core.repository

import com.nauta.interviewexample.core.model.Container
import java.util.*

interface ContainerRepository {
    fun saveAllForBooking(bookingId: UUID, containers: Set<Container>)
    fun findByBookingId(bookingId: UUID): Set<Container>
}