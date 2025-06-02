package com.nauta.interviewexample.controller

import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.repository.BookingRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.*

@ExtendWith(MockKExtension::class)
@WebMvcTest(BookingController::class)
class BookingControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    @TestConfiguration
    class MockConfig {
        @Bean
        fun bookingRepository(): BookingRepository = mockk(relaxed = true)
    }

    @Autowired
    lateinit var bookingRepository: BookingRepository

    private val clientId = "123e4567-e89b-12d3-a456-426614174000"

    @Test
    fun `should return booking if found`() {
        val booking = Booking.create(UUID.fromString(clientId), "BOOK123")
        booking.addAllContainers(setOf(Container.create("CONT1")))
        booking.addAllOrders(setOf(Order.create(UUID.fromString(clientId), booking.id, "PUR1", listOf("INV1"))))

        every { bookingRepository.findByCode("BOOK123", UUID.fromString(clientId)) } returns booking

        mockMvc.get("/api/bookings/BOOK123") {
            header("X-Client-ID", clientId)
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
        verify { bookingRepository.findByCode("BOOK123", UUID.fromString(clientId)) }
    }

    @Test
    fun `should return 404 if booking not found`() {
        every { bookingRepository.findByCode("BOOK404", UUID.fromString(clientId)) } returns null

        mockMvc.get("/api/bookings/BOOK404") {
            header("X-Client-ID", clientId)
        }
            .andExpect { status { isNotFound() } }
        verify { bookingRepository.findByCode("BOOK404", UUID.fromString(clientId)) }
    }
}