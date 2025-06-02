package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.repository.ContainerRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class FindContainersForClientActionTest {

    private val containerRepository = mockk<ContainerRepository>()
    private val action = FindContainersForClientAction(containerRepository)

    @Test
    fun `should return containers for client and call repository`() {
        val clientId = UUID.randomUUID()
        val containers = setOf(
            containerFor("CONT-1"),
            containerFor("CONT-2")
        )
        every { containerRepository.findByClientId(clientId) } returns containers

        val result = action.invoke(clientId)

        assertEquals(containers, result)
        verify(exactly = 1) { containerRepository.findByClientId(clientId) }
    }

    private fun containerFor(code: String): Container {
        return Container.create(code)
    }
}
