package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.repository.BookingRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class EmailRegisterAction(
    private val bookingRepository: BookingRepository
) {
    operator fun invoke(actionData: ActionData) {
        val booking = findBooking(actionData) ?: createBooking(actionData)
        val newContainers = calculateNewContainers(actionData.containers, booking)
        val newOrders = calculateNewOrders(actionData.orders, booking)

        booking.addAllContainers(newContainers)
        booking.addAllOrders(newOrders)

        bookingRepository.saveAllChanges(booking)
    }

    private fun findBooking(actionData: ActionData) =
        bookingRepository.findByCode(actionData.booking, actionData.clientId)

    private fun createBooking(actionData: ActionData) = Booking.create(actionData.clientId, actionData.booking)

    private fun calculateNewContainers(containers: List<String>, booking: Booking): Set<Container> {
        val existingContainers = booking.containers
        return containers.minus(existingContainers.map { it.code }.toSet()).map { Container.create(it) }.toSet()
    }

    private fun calculateNewOrders(orders: List<OrderData>, booking: Booking) : Set<Order> {
        val existingOrders = booking.orders
        val existingOrderPurchases = existingOrders.map { it.purchase }.toSet()
        val lala =  orders.filter { orderData ->
            !existingOrderPurchases.contains(orderData.purchase)
        }.map { it.toOrder(booking) }.toSet()
        return lala
    }

    data class ActionData(
        val clientId: UUID,
        val booking: String,
        val containers: List<String>,
        val orders: List<OrderData>
    )

    data class OrderData(
        val purchase: String,
        val invoices: List<String>
    ) {
        fun toOrder(booking: Booking): Order {
            val order = Order.create(
                clientId = booking.clientId,
                bookingId = booking.id,
                purchase = purchase,
                invoiceCodes = invoices
            )
            return order
        }
    }


}
