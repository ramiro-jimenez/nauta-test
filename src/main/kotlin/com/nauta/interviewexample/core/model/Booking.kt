package com.nauta.interviewexample.core.model

import com.nauta.interviewexample.util.UUIDExtensions.fromSeed
import java.util.*

data class Booking internal constructor(
    val id: UUID,
    val clientId: UUID,
    val code: String,
    val containers: MutableSet<Container>,
    val orders: MutableSet<Order>
) {

    fun addAllContainers(value: Set<Container>) {
        containers.addAll(value)
    }

    fun addAllOrders(value: Set<Order>) {
        orders.addAll(value)
    }

    companion object {
        fun create(
            clientId: UUID, 
            code: String, 
            containers: MutableSet<Container> = mutableSetOf(),
            orders: MutableSet<Order> = mutableSetOf()
        ): Booking {
            return Booking(
                id = fromSeed(clientId.toString() + code),
                clientId = clientId,
                code = code,
                containers = containers,
                orders = orders
            )
        }
    }
}
