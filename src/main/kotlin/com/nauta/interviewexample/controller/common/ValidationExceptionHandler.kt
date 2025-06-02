package com.nauta.interviewexample.controller.common

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<String> {
        val errors = ex.bindingResult.fieldErrors
            .joinToString("\n") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }

    @ExceptionHandler(Exception::class, Throwable::class)
    fun internalError(ex: Throwable): ResponseEntity<String> {
        logger.error("Unhandled error:", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error")
    }

    private companion object {
        val logger = LoggerFactory.getLogger(ValidationExceptionHandler::class.java)
    }
}