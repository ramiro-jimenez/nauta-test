package com.nauta.interviewexample.controller

import com.nauta.interviewexample.controller.response.ContainerResponse
import com.nauta.interviewexample.controller.response.OrderResponse
import com.nauta.interviewexample.core.action.FindContainersForPurchaseIdAction
import com.nauta.interviewexample.core.action.FindOrdersForClientAction
import com.nauta.interviewexample.core.model.Container
import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/orders")
@Validated
class OrderController(
    private val findOrdersForClientAction: FindOrdersForClientAction,
    private val findContainersForPurchaseIdAction: FindContainersForPurchaseIdAction
) {
    @GetMapping
    @ResponseBody
    fun getOrders(
        @RequestHeader("X-Client-ID") clientId: String
    ): ResponseEntity<List<OrderResponse>> {
        logger.info("Getting orders for client ID: $clientId")
        val orders = findOrdersForClientAction(UUID.fromString(clientId))
        return ResponseEntity.ok(orders.map { OrderResponse.from(it) })
    }


    @GetMapping("/{purchaseId}/containers")
    @ResponseBody
    fun getOrderContainers(
        @PathVariable @NotBlank purchaseId: String,
        @RequestHeader("X-Client-ID") clientId: String
    ): ResponseEntity<List<ContainerResponse>> {
        logger.info("Getting containers for purchaseId: $purchaseId and client ID: $clientId")
        val containers = findContainersForPurchaseIdAction(purchaseId, UUID.fromString(clientId))
        return ResponseEntity.ok(containers.map { ContainerResponse.from(it) })
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OrderController::class.java)
    }
}
