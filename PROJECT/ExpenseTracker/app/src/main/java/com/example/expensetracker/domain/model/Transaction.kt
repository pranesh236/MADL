package com.example.expensetracker.domain.model

import java.util.Date

data class Transaction(
    val id: Int = 0,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val note: String,
    val date: Long = System.currentTimeMillis(),
    val isRecurring: Boolean = false
)

enum class TransactionType {
    INCOME, EXPENSE
}
