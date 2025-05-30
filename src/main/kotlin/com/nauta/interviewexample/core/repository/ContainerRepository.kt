package com.nauta.interviewexample.core.repository

import com.nauta.interviewexample.core.model.Container

interface ContainerRepository {
    fun findByCode(code: String): Container?
    fun create(code: String): Container
}