package com.nauta.interviewexample.core.repository

import com.nauta.interviewexample.core.model.Invoice

interface InvoiceRepository {
    fun saveAll(invoices: List<Invoice>)
}
