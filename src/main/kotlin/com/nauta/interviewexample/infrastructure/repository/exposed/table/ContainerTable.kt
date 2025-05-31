package com.nauta.interviewexample.infrastructure.repository.exposed.table

import org.jetbrains.exposed.dao.id.UUIDTable

object ContainerTable: UUIDTable(
    name = "container",
    columnName = "id"
) {
    val code = varchar("code", 50).uniqueIndex()
}