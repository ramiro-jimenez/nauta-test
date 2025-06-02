package com.nauta.interviewexample.config.database

import com.nauta.interviewexample.infrastructure.repository.exposed.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value

@Configuration
class DatabaseConfiguration {

    @Bean
    fun database(
        @Value("\${spring.datasource.url}") url: String,
        @Value("\${spring.datasource.driver-class-name}") driver: String,
        @Value("\${spring.datasource.username}") user: String,
        @Value("\${spring.datasource.password}") password: String
    ): Database {
        return Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
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

