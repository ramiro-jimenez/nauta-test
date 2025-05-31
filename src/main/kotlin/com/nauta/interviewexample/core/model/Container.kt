package com.nauta.interviewexample.core.model

import com.nauta.interviewexample.util.UUIDExtensions.fromSeed
import java.util.UUID

data class Container internal constructor(
    val id: UUID,
    val code: String
) {
    companion object {
        fun create(code: String): Container {
            return Container(
                id = fromSeed(code),
                code = code
            )
        }
    }
}