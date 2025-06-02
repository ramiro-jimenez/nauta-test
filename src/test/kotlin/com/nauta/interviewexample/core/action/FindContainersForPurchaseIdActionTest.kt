package com.nauta.interviewexample.core.action

import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.repository.ContainerRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class FindContainersForPurchaseIdActionTest {

    private val containerRepository = mockk<ContainerRepository>()
    private val action = FindContainersForPurchaseIdAction(containerRepository)

    @Test
    fun `should return containers for purchaseId and clientId and call repository`() {
        val purchaseId = "purchase-123"
        val clientId = UUID.randomUUID()
        val containers = setOf(
            containerFor("CONT-1"),
            containerFor("CONT-2"),
        )
        every { containerRepository.findByPurchaseId(purchaseId, clientId) } returns containers

        val result = action.invoke(purchaseId, clientId)

        assertEquals(containers, result)
        verify(exactly = 1) { containerRepository.findByPurchaseId(purchaseId, clientId) }
    }

    private fun containerFor(code: String): Container {
        return Container.create(code)
    }
}
