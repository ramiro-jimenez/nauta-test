package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.repository.BookingRepository
import com.nauta.interviewexample.core.repository.ContainerRepository
import com.nauta.interviewexample.core.repository.OrderRepository
import org.springframework.stereotype.Component

@Component
class EmailRegisterAction(
    private val bookingRepository: BookingRepository,
    private val containerRepository: ContainerRepository,
    private val orderRepository: OrderRepository
) {
    data class ActionData(
        val booking: String,
        val containers: List<String>,
        val orders: List<OrderData>,
        val clientId: String
    )

    data class OrderData(
        val purchase: String,
        val invoices: List<String>
    )

    operator fun invoke(actionData: ActionData) {
        // 1. Booking
        val booking = bookingRepository.findByCode(actionData.booking)
            ?: bookingRepository.create(actionData.booking)

        // 2. Containers
        actionData.containers.forEach { containerCode ->
            containerRepository.findByCode(containerCode)
                ?: containerRepository.create(containerCode)
        }

        // 3. Orders
        actionData.orders.forEach { orderData ->
            orderRepository.findByPurchase(orderData.purchase)
                ?: orderRepository.create(orderData.purchase)
        }
    }
}
