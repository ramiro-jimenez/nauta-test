package com.nauta.interviewexample.core.service

import com.nauta.interviewexample.core.model.Booking

interface BookingService {
    fun findByCode(code: String): Booking?
    fun save(booking: Booking)
}