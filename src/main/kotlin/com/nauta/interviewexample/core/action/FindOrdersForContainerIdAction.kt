package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.repository.OrderRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class FindOrdersForContainerIdAction(
    private val orderRepository: OrderRepository
) {

    operator fun invoke(containerId: String, clientId: UUID): Set<Order> {
        return orderRepository.findByContainerId(containerId, clientId)
    }
}