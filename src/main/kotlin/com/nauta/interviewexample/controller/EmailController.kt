package com.nauta.interviewexample.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import com.nauta.interviewexample.core.action.EmailRegisterAction
import java.util.*

@RestController
@RequestMapping("/api/email")
class EmailController(
    private val emailRegisterAction: EmailRegisterAction
) {

    @PostMapping
    fun sendEmail(
        @RequestBody request: EmailRequest,
        @RequestHeader("X-Client-ID") clientId: String
    ): ResponseEntity<String> {
        val actionData = request.toActionData(clientId)
        emailRegisterAction(actionData)
        return ResponseEntity.ok("Email recibido para booking: ${request.booking} del cliente: $clientId")
    }

}

data class EmailRequest(
    val booking: String,
    val containers: List<ContainerDTO>?,
    val orders: List<OrderDTO>?
)

data class ContainerDTO(
    val container: String
)

data class OrderDTO(
    val purchase: String,
    val invoices: List<InvoiceDTO>
)

data class InvoiceDTO(
    val invoice: String
)

fun EmailRequest.toActionData(clientId: String): EmailRegisterAction.ActionData =
    EmailRegisterAction.ActionData(
        booking = booking,
        containers = containers?.map { it.container } ?: emptyList(),
        orders = orders?.map { order ->
            EmailRegisterAction.OrderData(
                purchase = order.purchase,
                invoices = order.invoices.map { it.invoice }
            )
        } ?: emptyList(),
        clientId = UUID.fromString(clientId)
    )
