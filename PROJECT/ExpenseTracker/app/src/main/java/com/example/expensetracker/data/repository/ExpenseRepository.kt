package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseRepository @Inject constructor(private val expenseDao: ExpenseDao) {
    fun getAllExpenses(): Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()
    
    fun getExpensesByMonth(month: Int, year: Int): Flow<List<ExpenseEntity>> = 
        expenseDao.getExpensesByMonth(month, year)

    fun getTotalExpenseByMonth(month: Int, year: Int): Flow<Double?> = 
        expenseDao.getTotalExpenseByMonth(month, year)

    fun getTotalIncomeByMonth(month: Int, year: Int): Flow<Double?> = 
        expenseDao.getTotalIncomeByMonth(month, year)

    suspend fun insertExpense(expense: ExpenseEntity) = expenseDao.insertExpense(expense)
    suspend fun updateExpense(expense: ExpenseEntity) = expenseDao.updateExpense(expense)
    suspend fun deleteExpense(expense: ExpenseEntity) = expenseDao.deleteExpense(expense)
    suspend fun getExpenseById(id: Int): ExpenseEntity? = expenseDao.getExpenseById(id)
}
