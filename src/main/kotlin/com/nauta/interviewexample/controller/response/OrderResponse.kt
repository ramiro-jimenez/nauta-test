package com.nauta.interviewexample.controller.response

import com.nauta.interviewexample.core.model.Order

class OrderResponse(
    val purchase: String,
    val invoices: List<InvoiceResponse>
) {
    companion object {
        fun from(order: Order) = OrderResponse(
            purchase = order.purchase,
            invoices = order.invoices.map { InvoiceResponse.from(it) }
        )
    }
}