package com.nauta.interviewexample.controller

import com.nauta.interviewexample.controller.response.ContainerResponse
import com.nauta.interviewexample.controller.response.OrderResponse
import com.nauta.interviewexample.core.action.FindContainersForClientAction
import com.nauta.interviewexample.core.action.FindOrdersForContainerIdAction
import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.model.Order
import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/containers")
@Validated
class ContainerController(
    private val findContainersForClientAction: FindContainersForClientAction,
    private val findOrdersForContainerIdAction: FindOrdersForContainerIdAction
) {
    @GetMapping
    @ResponseBody
    fun getContainers(
        @RequestHeader("X-Client-ID") clientId: String
    ): ResponseEntity<List<ContainerResponse>> {
        logger.info("Getting containers for client ID: $clientId")
        val containers = findContainersForClientAction(UUID.fromString(clientId))
        return ResponseEntity.ok(containers.map { ContainerResponse.from(it) })
    }

    @GetMapping("/{containerId}/orders")
    @ResponseBody
    fun getContainerOrders(
        @PathVariable @NotBlank containerId: String,
        @RequestHeader("X-Client-ID") clientId: String
    ): ResponseEntity<List<OrderResponse>> {
        logger.info("Getting orders for containerId: $containerId and client ID: $clientId")
        val orders = findOrdersForContainerIdAction(containerId, UUID.fromString(clientId))
        return ResponseEntity.ok(orders.map { OrderResponse.from(it) })
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ContainerController::class.java)
    }
}