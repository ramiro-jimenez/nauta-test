package com.nauta.interviewexample.controller

import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.repository.BookingRepository
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingRepository: BookingRepository
) {

    @GetMapping("/{code}")
    fun getBooking(@PathVariable code: String): ResponseEntity<Booking> {
        logger.info("Fetching booking with code: $code")
        val booking = bookingRepository.findByCode(code)
        return if (booking != null) {
            ResponseEntity.ok(booking)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BookingController::class.java)
    }
}