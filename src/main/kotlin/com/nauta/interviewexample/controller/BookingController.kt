package com.nauta.interviewexample.controller

import com.nauta.interviewexample.controller.response.BookingResponse
import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.repository.BookingRepository
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingRepository: BookingRepository
) {

    @GetMapping("/{code}")
    fun getBooking(
        @PathVariable code: String,
        @RequestHeader("X-Client-ID") clientId: String
    ): ResponseEntity<BookingResponse> {
        logger.info("Fetching booking with code: $code and client ID: $clientId")
        val booking = bookingRepository.findByCode(code, UUID.fromString(clientId))
        return if (booking != null) {
            ResponseEntity.ok(BookingResponse.from(booking))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BookingController::class.java)
    }
}