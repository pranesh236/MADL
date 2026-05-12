package com.example.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val type: String,
    val category: String,
    val note: String,
    val date: Long,
    val isRecurring: Boolean
) {
    fun toDomain(): Transaction = Transaction(
        id = id,
        amount = amount,
        type = TransactionType.valueOf(type),
        category = category,
        note = note,
        date = date,
        isRecurring = isRecurring
    )

    companion object {
        fun fromDomain(transaction: Transaction): TransactionEntity = TransactionEntity(
            id = transaction.id,
            amount = transaction.amount,
            type = transaction.type.name,
            category = transaction.category,
            note = transaction.note,
            date = transaction.date,
            isRecurring = transaction.isRecurring
        )
    }
}
