package com.nauta.interviewexample.core.model

import java.util.*

data class Booking internal constructor(
    val id: UUID,
    val clientId: UUID,
    val code: String,
    val containers: MutableList<Container>,
    val orders: MutableList<Order>
) {
    companion object {
        fun create(
            clientId: UUID, 
            code: String, 
            containers: MutableList<Container> = mutableListOf(), 
            orders: MutableList<Order> = mutableListOf()
        ): Booking {
            return Booking(
                id = UUID.fromString(clientId.toString() + code),
                clientId = clientId,
                code = code,
                containers = containers,
                orders = orders
            )
        }
    }
}
