package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.model.Booking
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.model.Invoice
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.service.BookingService
import com.nauta.interviewexample.core.repository.ContainerRepository
import com.nauta.interviewexample.core.repository.InvoiceRepository
import com.nauta.interviewexample.core.repository.OrderRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class EmailRegisterAction(
    private val bookingService: BookingService,
    private val containerRepository: ContainerRepository,
    private val orderRepository: OrderRepository,
    private val invoiceRepository: InvoiceRepository
) {
    operator fun invoke(actionData: ActionData) {
        val booking = findBooking(actionData.booking) ?: createBooking(actionData)
        val newContainers = calculateNewContainers(actionData.containers, booking)
        val newOrders = calculateNewOrders(actionData.orders, booking)
        val newInvoices = getNewInvoices(newOrders)

        booking.addAllContainers(newContainers)
        booking.addAllOrders(newOrders)

        containerRepository.saveAll(newContainers)
        invoiceRepository.saveAll(newInvoices)
        orderRepository.saveAll(newOrders)
        bookingService.save(booking)
    }

    private fun findBooking(bookingCode: String) = bookingService.findByCode(bookingCode)

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

    private fun getNewInvoices(newOrders: Set<Order>) = newOrders.flatMap { it.invoices }

    data class ActionData(
        val booking: String,
        val containers: List<String>,
        val orders: List<OrderData>,
        val clientId: UUID
    )

    data class OrderData(
        val purchase: String,
        val invoices: List<String>
    ) {
        fun toOrder(booking: Booking): Order {
            val order = Order.create(
                clientId = booking.clientId,
                bookingId = booking.id,
                purchase = purchase
            )
            invoices.forEach {
                order.addInvoice(Invoice.create(
                    clientId = booking.clientId,
                    bookingId = booking.id,
                    orderId = order.id,
                    code = it)
                )
            }
            return order
        }
    }


}
