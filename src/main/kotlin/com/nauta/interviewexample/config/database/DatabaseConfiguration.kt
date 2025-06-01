package com.nauta.interviewexample.config.database

import com.nauta.interviewexample.infrastructure.repository.exposed.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfiguration {

    @Bean
    fun database(): Database {
        return Database.connect(
            url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        )
    }

    @Bean
    fun initDatabaseSchema(database: Database) = ApplicationRunner {
        transaction(database) {
            SchemaUtils.create(
                BookingTable,
                BookingContainerTable,
                ContainerTable,
                InvoiceTable,
                OrderTable,)
        }
    }
}