package com.nauta.interviewexample.core.action.exception

class PurchaseNotFoundException(
    purchaseId: String
) : Throwable("Cannot found any booking related to purchaseId: $purchaseId")
