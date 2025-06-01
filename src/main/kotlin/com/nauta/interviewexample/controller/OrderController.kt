package com.nauta.interviewexample.controller

import com.nauta.interviewexample.core.action.FindContainersForPurchaseIdAction
import com.nauta.interviewexample.core.action.FindOrdersForClientAction
import jakarta.validation.constraints.NotBlank
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
    ) = ResponseEntity.ok(findOrdersForClientAction(UUID.fromString(clientId)))

    @GetMapping("/{purchaseId}/containers")
    @ResponseBody
    fun getOrderContainers(
        @PathVariable @NotBlank purchaseId: String,
        @RequestHeader("X-Client-ID") clientId: String
    ) = ResponseEntity.ok(findContainersForPurchaseIdAction(purchaseId, UUID.fromString(clientId)))
}
