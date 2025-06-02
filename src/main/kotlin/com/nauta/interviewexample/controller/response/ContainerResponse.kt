package com.nauta.interviewexample.controller.response

import com.nauta.interviewexample.core.model.Container

class ContainerResponse(
    val container: String
) {
    companion object {
        fun from(container: Container): ContainerResponse {
            return ContainerResponse(container = container.code)
        }
    }
}

