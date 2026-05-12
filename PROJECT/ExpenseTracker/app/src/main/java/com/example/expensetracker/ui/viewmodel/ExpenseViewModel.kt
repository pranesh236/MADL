package com.example.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    // All transactions for Dashboard (Newest first)
    val allTransactions: StateFlow<List<Transaction>> = repository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dashboard calculations from ALL data
    val totalIncome: StateFlow<Double> = allTransactions.map { list ->
        list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpense: StateFlow<Double> = allTransactions.map { list ->
        list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalBalance: StateFlow<Double> = combine(totalIncome, totalExpense) { income, expense ->
        income - expense
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Filtering logic for Analytics
    fun getFilteredTransactions(period: String): Flow<List<Transaction>> {
        return allTransactions.map { transactions ->
            val now = Calendar.getInstance()
            when (period.uppercase()) {
                "WEEK" -> {
                    val startOfWeek = now.clone() as Calendar
                    startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    startOfWeek.set(Calendar.HOUR_OF_DAY, 0)
                    startOfWeek.set(Calendar.MINUTE, 0)
                    startOfWeek.set(Calendar.SECOND, 0)
                    startOfWeek.set(Calendar.MILLISECOND, 0)
                    transactions.filter { it.date >= startOfWeek.timeInMillis }
                }
                "MONTH" -> {
                    val startOfMonth = now.clone() as Calendar
                    startOfMonth.set(Calendar.DAY_OF_MONTH, 1)
                    startOfMonth.set(Calendar.HOUR_OF_DAY, 0)
                    startOfMonth.set(Calendar.MINUTE, 0)
                    startOfMonth.set(Calendar.SECOND, 0)
                    startOfMonth.set(Calendar.MILLISECOND, 0)
                    transactions.filter { it.date >= startOfMonth.timeInMillis }
                }
                "YEAR" -> {
                    val startOfYear = now.clone() as Calendar
                    startOfYear.set(Calendar.DAY_OF_YEAR, 1)
                    startOfYear.set(Calendar.HOUR_OF_DAY, 0)
                    startOfYear.set(Calendar.MINUTE, 0)
                    startOfYear.set(Calendar.SECOND, 0)
                    startOfYear.set(Calendar.MILLISECOND, 0)
                    transactions.filter { it.date >= startOfYear.timeInMillis }
                }
                else -> transactions
            }
        }
    }

    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.insertTransaction(transaction)
    }

    fun updateTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.updateTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.deleteTransaction(transaction)
    }

    fun clearAllData() = viewModelScope.launch {
        repository.deleteAllTransactions()
    }
    
    suspend fun getTransactionById(id: Int) = repository.getTransactionById(id)
}
