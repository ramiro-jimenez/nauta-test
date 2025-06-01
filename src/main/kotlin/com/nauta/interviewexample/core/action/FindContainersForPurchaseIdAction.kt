package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.action.exception.PurchaseNotFoundException
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.repository.BookingRepository
import com.nauta.interviewexample.core.repository.ContainerRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class FindContainersForPurchaseIdAction(
    private val bookingRepository: BookingRepository,
    private val containerRepository: ContainerRepository,
) {

    operator fun invoke(purchaseId: String, clientId: UUID): Set<Container> {
        val bookingId = bookingRepository.findBookingIdByPurchaseId(purchaseId, clientId)
            ?: throwNotFoundException(purchaseId)
        return containerRepository.findByBookingId(bookingId)
    }

    private fun throwNotFoundException(purchaseId: String): Nothing {
        throw PurchaseNotFoundException(purchaseId)
    }
}