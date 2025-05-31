package com.nauta.interviewexample.core.model

import java.util.*

data class Invoice internal constructor(
    val id: UUID,
    val code: String
) {
    companion object {
        fun create(code: String): Invoice {
            return Invoice(
                id = UUID.fromString(code),
                code = code
            )
        }
    }
}