package com.nauta.interviewexample.core.repository

import com.nauta.interviewexample.core.model.Booking
import java.util.*

interface BookingRepository {
    fun findByCode(code: String, clientId: UUID): Booking?
    fun saveAllChanges(booking: Booking)
}