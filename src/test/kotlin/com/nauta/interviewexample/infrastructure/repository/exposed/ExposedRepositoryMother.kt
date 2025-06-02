package com.nauta.interviewexample.infrastructure.repository.exposed

import com.nauta.interviewexample.infrastructure.repository.exposed.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedRepositoryMother {
    companion object {
        fun database(): Database {
            val db = Database.connect("jdbc:h2:mem:bookingtest;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
            transaction(db) {
                SchemaUtils.create(
                    BookingTable,
                    ContainerTable,
                    BookingContainerTable,
                    OrderTable,
                    InvoiceTable
                )
            }
            return db
        }

        fun drop(db: Database) {
            transaction(db) {
                SchemaUtils.drop(
                    BookingTable,
                    ContainerTable,
                    BookingContainerTable,
                    OrderTable,
                    InvoiceTable
                )
            }
        }
    }
}