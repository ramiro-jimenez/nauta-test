package com.nauta.interviewexample.controller

import com.nauta.interviewexample.core.action.FindContainersForPurchaseIdAction
import com.nauta.interviewexample.core.action.FindOrdersForClientAction
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.model.Order
import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.math.log

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
    ): ResponseEntity<Set<Order>> {
        logger.info("Getting orders for client ID: $clientId")
        return ResponseEntity.ok(findOrdersForClientAction(UUID.fromString(clientId)))
    }


    @GetMapping("/{purchaseId}/containers")
    @ResponseBody
    fun getOrderContainers(
        @PathVariable @NotBlank purchaseId: String,
        @RequestHeader("X-Client-ID") clientId: String
    ): ResponseEntity<Set<Container>> {
        logger.info("Getting containers for purchaseId: $purchaseId and client ID: $clientId")
        return ResponseEntity.ok(findContainersForPurchaseIdAction(purchaseId, UUID.fromString(clientId)))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OrderController::class.java)
    }
}
