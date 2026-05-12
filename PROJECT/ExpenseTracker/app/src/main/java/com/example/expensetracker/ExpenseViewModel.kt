package com.example.expensetracker

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExpenseRepository
    val allExpenses: LiveData<List<ExpenseEntity>>
    val totalExpense: LiveData<Double?>
    val totalIncome: LiveData<Double?>

    private val _searchQuery = MutableStateFlow("")
    private val _filteredExpenses = MutableLiveData<List<ExpenseEntity>>()
    val filteredExpenses: LiveData<List<ExpenseEntity>> = _filteredExpenses

    init {
        val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(expenseDao)
        allExpenses = repository.allExpenses.asLiveData()
        totalExpense = repository.totalExpense.asLiveData()
        totalIncome = repository.totalIncome.asLiveData()

        viewModelScope.launch {
            _searchQuery.collectLatest { query ->
                if (query.isEmpty()) {
                    repository.allExpenses.collect { _filteredExpenses.postValue(it) }
                } else {
                    repository.searchExpenses(query).collect { _filteredExpenses.postValue(it) }
                }
            }
        }
    }

    fun insert(expense: ExpenseEntity) = viewModelScope.launch {
        repository.insert(expense)
    }

    fun update(expense: ExpenseEntity) = viewModelScope.launch {
        repository.update(expense)
    }

    fun delete(expense: ExpenseEntity) = viewModelScope.launch {
        repository.delete(expense)
    }

    fun search(query: String) {
        _searchQuery.value = query
    }

    fun filterByDateRange(start: Long, end: Long) = viewModelScope.launch {
        repository.getExpensesByDateRange(start, end).collect {
            _filteredExpenses.postValue(it)
        }
    }
}
