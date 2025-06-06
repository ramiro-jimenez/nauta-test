package com.nauta.interviewexample.controller

import com.nauta.interviewexample.core.action.EmailRegisterAction
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/email")
@Validated
class EmailController(
    private val emailRegisterAction: EmailRegisterAction
) {

    @PostMapping
    fun sendEmail(
        @RequestBody @Valid request: EmailRequest,
        @RequestHeader("X-Client-ID") clientId: String
    ): ResponseEntity<String> {
        logger.info("Received email request for booking: ${request.booking} with client ID: $clientId")

        emailRegisterAction(request.toActionData(clientId))

        return ResponseEntity.ok("Email processed successfully")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(EmailController::class.java)
    }
}

data class EmailRequest(
    @field:NotBlank val booking: String,
    @field:Valid val containers: List<ContainerDTO>?,
    @field:Valid val orders: List<OrderDTO>?
)

data class ContainerDTO(
    @field:NotBlank val container: String
)

data class OrderDTO(
    @field:NotBlank val purchase: String,
    @field:Valid val invoices: List<InvoiceDTO>
)

data class InvoiceDTO(
    @field:NotBlank val invoice: String
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
