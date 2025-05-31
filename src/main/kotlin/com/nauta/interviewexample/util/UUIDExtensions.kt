package com.nauta.interviewexample.util

import java.util.*

object UUIDExtensions {
    @JvmStatic
    fun fromSeed(seed: String): UUID {
        return UUID.nameUUIDFromBytes(seed.toByteArray())
    }
}