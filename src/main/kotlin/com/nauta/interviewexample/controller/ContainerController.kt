package com.nauta.interviewexample.controller

import com.nauta.interviewexample.core.action.FindContainersForClientAction
import com.nauta.interviewexample.core.action.FindOrdersForContainerIdAction
import jakarta.validation.constraints.NotBlank
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
    ) = ResponseEntity.ok(findContainersForClientAction(UUID.fromString(clientId)))

    @GetMapping("/{containerId}/orders")
    @ResponseBody
    fun getContainerOrders(
        @PathVariable @NotBlank containerId: String,
        @RequestHeader("X-Client-ID") clientId: String
    ) = ResponseEntity.ok(findOrdersForContainerIdAction(containerId, UUID.fromString(clientId)))
}