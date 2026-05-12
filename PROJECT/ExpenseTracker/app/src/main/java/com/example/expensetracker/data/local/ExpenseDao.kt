package com.example.expensetracker.data.local

import androidx.room.*
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE month = :month AND year = :year ORDER BY date DESC")
    fun getExpensesByMonth(month: Int, year: Int): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses WHERE type = 'Expense' AND month = :month AND year = :year")
    fun getTotalExpenseByMonth(month: Int, year: Int): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE type = 'Income' AND month = :month AND year = :year")
    fun getTotalIncomeByMonth(month: Int, year: Int): Flow<Double?>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Int): ExpenseEntity?
}
