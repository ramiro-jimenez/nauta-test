package com.nauta.interviewexample.core.repository

import com.nauta.interviewexample.core.model.Container

interface ContainerRepository {
    fun findByCode(code: String): Container?
    fun findByCode(code: List<String>): List<Container>
    fun saveAll(containers: Set<Container>)
}