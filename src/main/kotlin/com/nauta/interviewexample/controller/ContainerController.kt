package com.nauta.interviewexample.controller

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
    ): ResponseEntity<Set<Container>> {
        logger.info("Getting containers for client ID: $clientId")
        return ResponseEntity.ok(findContainersForClientAction(UUID.fromString(clientId)))
    }

    @GetMapping("/{containerId}/orders")
    @ResponseBody
    fun getContainerOrders(
        @PathVariable @NotBlank containerId: String,
        @RequestHeader("X-Client-ID") clientId: String
    ): ResponseEntity<Set<Order>> {
        logger.info("Getting orders for containerId: $containerId and client ID: $clientId")
        return ResponseEntity.ok(findOrdersForContainerIdAction(containerId, UUID.fromString(clientId)))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ContainerController::class.java)
    }
}