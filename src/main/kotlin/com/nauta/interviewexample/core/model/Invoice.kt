package com.nauta.interviewexample.core.model

import com.nauta.interviewexample.util.UUIDExtensions.fromSeed
import java.util.*

data class Invoice internal constructor(
    val id: UUID,
    val clientId: UUID,
    val bookingId: UUID,
    val orderId: UUID,
    val code: String
) {
    companion object {
        fun create(clientId: UUID, bookingId: UUID, orderId: UUID, code: String): Invoice {
            return Invoice(
                id = fromSeed(clientId.toString() + bookingId.toString() + orderId.toString() + code),
                clientId = clientId,
                bookingId = bookingId,
                orderId = orderId,
                code = code
            )
        }
    }
}