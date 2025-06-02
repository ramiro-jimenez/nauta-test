package com.nauta.interviewexample.controller.response

import com.nauta.interviewexample.core.model.Booking

class BookingResponse(
    val booking: String,
    val containers: List<ContainerResponse>,
    val orders: List<OrderResponse>
) {
    companion object {
        fun from(booking: Booking): BookingResponse {
            return BookingResponse(
                booking = booking.code,
                containers = booking.containers.map { ContainerResponse.from(it) },
                orders = booking.orders.map { OrderResponse.from(it) }
            )
        }
    }
}