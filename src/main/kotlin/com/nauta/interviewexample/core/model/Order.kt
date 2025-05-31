package com.nauta.interviewexample.core.model

import java.util.*

data class Order internal constructor(
    val id: UUID,
    val purchase: String,
    val invoices: MutableList<Invoice>
) {
    companion object {
        fun create(
            purchase: String,
            invoices: MutableList<Invoice> = mutableListOf()
        ): Order {
            return Order(
                id = UUID.fromString(purchase),
                purchase = purchase,
                invoices = invoices
            )
        }
    }
}