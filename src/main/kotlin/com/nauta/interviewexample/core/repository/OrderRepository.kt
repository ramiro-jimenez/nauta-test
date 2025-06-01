package com.nauta.interviewexample.core.repository

import com.nauta.interviewexample.core.model.Order
import java.util.*

interface OrderRepository {
    fun saveAllForBooking(orders: Set<Order>)
    fun findByBookingId(bookingId: UUID): Set<Order>
    fun findByClientId(clientId: UUID): Set<Order>
}