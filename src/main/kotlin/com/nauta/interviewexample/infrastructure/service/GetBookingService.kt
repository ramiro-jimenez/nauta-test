package com.nauta.interviewexample.infrastructure.service

import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.service.BookingService

class GetBookingService (private val bookingRepository: BookingService) {

    fun findByCode(code: String): Booking? {
        val booking = bookingRepository.findByCode(code) ?: return null
        val containers = containerRepository.findByBookingId(booking.id)
        val orders = orderRepository.findByBookingId(booking.id)

        return inflateBooking(booking, containers, orders)
    }

    private fun inflateBooking(booking: Booking, containers: Set<Container>, orders: Set<Order>) =
        booking.copy(
            containers = containers.toMutableSet(),
            orders = orders.toMutableSet()
        )

}
