package com.nauta.interviewexample.core.repository

import com.nauta.interviewexample.core.model.Booking
import java.util.UUID

interface BookingRepository {
    fun findByCode(code: String): Booking?
    fun saveAllChanges(booking: Booking)
    fun findBookingIdByPurchaseId(purchaseId: String, clientId: UUID): UUID?
}