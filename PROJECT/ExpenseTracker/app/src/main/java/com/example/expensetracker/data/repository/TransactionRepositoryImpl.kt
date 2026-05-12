package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.dao.TransactionDao
import com.example.expensetracker.data.local.entity.TransactionEntity
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> {
        return dao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByMonth(month: Int, year: Int): Flow<List<Transaction>> {
        return dao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }.filter {
                val cal = java.util.Calendar.getInstance().apply { timeInMillis = it.date }
                cal.get(java.util.Calendar.MONTH) == month && cal.get(java.util.Calendar.YEAR) == year
            }
        }
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(TransactionEntity.fromDomain(transaction))
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(TransactionEntity.fromDomain(transaction))
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(TransactionEntity.fromDomain(transaction))
    }

    override suspend fun getTransactionById(id: Int): Transaction? {
        return dao.getTransactionById(id)?.toDomain()
    }

    override suspend fun deleteAllTransactions() {
        dao.deleteAllTransactions()
    }
}
