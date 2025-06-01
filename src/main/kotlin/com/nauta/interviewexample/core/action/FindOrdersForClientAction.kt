package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.repository.OrderRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class FindOrdersForClientAction(
    private val orderRepository: OrderRepository
) {

    operator fun invoke(clientId: UUID): Set<Order> {
        return orderRepository.findByClientId(clientId)
    }
}