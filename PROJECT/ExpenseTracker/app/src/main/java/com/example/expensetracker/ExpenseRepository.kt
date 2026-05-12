package com.example.expensetracker

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    val allExpenses: Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()
    val totalExpense: Flow<Double?> = expenseDao.getTotalExpense()
    val totalIncome: Flow<Double?> = expenseDao.getTotalIncome()

    suspend fun insert(expense: ExpenseEntity) {
        expenseDao.insertExpense(expense)
    }

    suspend fun update(expense: ExpenseEntity) {
        expenseDao.updateExpense(expense)
    }

    suspend fun delete(expense: ExpenseEntity) {
        expenseDao.deleteExpense(expense)
    }

    suspend fun getExpenseById(id: Int): ExpenseEntity? {
        return expenseDao.getExpenseById(id)
    }

    fun searchExpenses(query: String): Flow<List<ExpenseEntity>> {
        return expenseDao.searchExpenses(query)
    }

    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate)
    }
}
