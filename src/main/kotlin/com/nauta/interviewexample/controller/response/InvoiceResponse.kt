package com.nauta.interviewexample.controller.response

import com.nauta.interviewexample.core.model.Invoice

class InvoiceResponse(
    val invoice: String
) {
    companion object {
        fun from(invoice: Invoice) = InvoiceResponse(invoice.code)
    }
}