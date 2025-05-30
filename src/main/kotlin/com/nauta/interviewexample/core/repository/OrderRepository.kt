package com.nauta.interviewexample.core.repository

import com.nauta.interviewexample.core.model.Order

interface OrderRepository {
    fun findByPurchase(purchase: String): Order?
    fun create(purchase: String): Order
}