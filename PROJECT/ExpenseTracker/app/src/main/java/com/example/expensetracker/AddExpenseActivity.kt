package com.example.expensetracker

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.expensetracker.databinding.ActivityAddExpenseBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var viewModel: ExpenseViewModel
    private var expenseId: Int = -1
    private var currentExpense: ExpenseEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        viewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        setupTypeSelector()
        setupCategorySpinner()

        expenseId = intent.getIntExtra("EXPENSE_ID", -1)
        if (expenseId != -1) {
            setupEditMode()
        }

        binding.btnSave.setOnClickListener {
            saveExpense()
        }

        binding.btnDelete.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun setupTypeSelector() {
        val types = arrayOf("Expense", "Income")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
        binding.spinnerType.adapter = adapter
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf("Food", "Travel", "Bills", "Entertainment", "Shopping", "Others")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategory.adapter = adapter
    }

    private fun setupEditMode() {
        supportActionBar?.title = "Edit Transaction"
        binding.btnSave.text = "Update"
        binding.btnDelete.visibility = View.VISIBLE

        lifecycleScope.launch {
            val expense = withContext(Dispatchers.IO) {
                ExpenseDatabase.getDatabase(this@AddExpenseActivity).expenseDao().getExpenseById(expenseId)
            }
            expense?.let {
                currentExpense = it
                binding.etAmount.setText(it.amount.toString())
                binding.etNote.setText(it.note)
                
                val types = arrayOf("Expense", "Income")
                binding.spinnerType.setSelection(types.indexOf(it.type))
                
                val categories = arrayOf("Food", "Travel", "Bills", "Entertainment", "Shopping", "Others")
                binding.spinnerCategory.setSelection(categories.indexOf(it.category))
            }
        }
    }

    private fun saveExpense() {
        val amountStr = binding.etAmount.text.toString()
        val note = binding.etNote.text.toString()
        val type = binding.spinnerType.selectedItem.toString()
        val category = binding.spinnerCategory.selectedItem.toString()

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull() ?: 0.0
        val date = currentExpense?.date ?: System.currentTimeMillis()

        val expense = if (expenseId == -1) {
            ExpenseEntity(amount = amount, type = type, category = category, note = note, date = date)
        } else {
            ExpenseEntity(id = expenseId, amount = amount, type = type, category = category, note = note, date = date)
        }

        if (expenseId == -1) {
            viewModel.insert(expense)
        } else {
            viewModel.update(expense)
        }

        Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Transaction")
            .setMessage("Are you sure you want to delete this transaction?")
            .setPositiveButton("Delete") { _, _ ->
                currentExpense?.let {
                    viewModel.delete(it)
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
