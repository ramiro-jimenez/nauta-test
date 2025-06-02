package com.nauta.interviewexample.core.repository

import com.nauta.interviewexample.core.model.Booking

interface BookingRepository {
    fun findByCode(code: String): Booking?
    fun saveAllChanges(booking: Booking)
}