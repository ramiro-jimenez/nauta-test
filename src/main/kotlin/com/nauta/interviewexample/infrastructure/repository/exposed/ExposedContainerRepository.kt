package com.nauta.interviewexample.infrastructure.repository.exposed

import com.nauta.interviewexample.core.model.Container
import com.nauta.interviewexample.core.model.Order
import com.nauta.interviewexample.core.repository.ContainerRepository
import com.nauta.interviewexample.infrastructure.repository.exposed.table.BookingContainerTable
import com.nauta.interviewexample.infrastructure.repository.exposed.table.BookingTable
import com.nauta.interviewexample.infrastructure.repository.exposed.table.ContainerTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ExposedContainerRepository(private val database: Database) : ContainerRepository {

    override fun findByBookingId(bookingId: UUID): Set<Container> {
        return transaction(database) {
            (ContainerTable innerJoin BookingContainerTable)
                .select(ContainerTable.columns)
                .where { BookingContainerTable.booking eq bookingId }
                .map { toContainer(it) }.toSet()
        }
    }

    override fun findByClientId(clientId: UUID): Set<Container> {
        return transaction(database) {
            (BookingTable innerJoin BookingContainerTable innerJoin ContainerTable)
                .select(ContainerTable.columns)
                .withDistinct(true)
                .where { BookingTable.clientId eq clientId }
                .map { toContainer(it) }.toSet()
        }
    }

    override fun saveAllForBooking(bookingId: UUID, containers: Set<Container>) {
        transaction(database) {
            saveNotExistingContainers(bookingId, containers)
            saveBookingContainers(containers, bookingId)
        }
    }

    private fun saveNotExistingContainers(bookingId: UUID, containers: Set<Container>) {
        transaction(database) {
            val existingContainers = ContainerTable.selectAll()
                .where { ContainerTable.code inList containers.map { it.code } }
                .map { it[ContainerTable.id].value }

            val newContainers = containers.filterNot { it.id in existingContainers }.toSet()

            if (newContainers.isNotEmpty()) {
                saveAll(newContainers)
            }
        }
    }

    private fun saveAll(containers: Set<Container>) {
        transaction(database) {
            ContainerTable.batchUpsert(containers, shouldReturnGeneratedValues = false) { container ->
                this[ContainerTable.id] = container.id
                this[ContainerTable.code] = container.code
            }
        }
    }

    private fun saveBookingContainers(
        containers: Set<Container>,
        bookingId: UUID
    ) {
        transaction(database) {
            BookingContainerTable.batchUpsert(containers, shouldReturnGeneratedValues = false)
            { container ->
                this[BookingContainerTable.booking] = bookingId
                this[BookingContainerTable.container] = container.id
            }
        }
    }

    private fun toContainer(it: ResultRow) = Container(
        id = it[ContainerTable.id].value,
        code = it[ContainerTable.code]
    )
}

