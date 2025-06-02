package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.repository.ContainerRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class FindContainersForClientAction(
    private val containerRepository: ContainerRepository
) {
    operator fun invoke(clientId: UUID): Set<Container> {
        return containerRepository.findByClientId(clientId)
    }
}