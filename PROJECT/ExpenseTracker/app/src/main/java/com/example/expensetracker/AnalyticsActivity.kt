package com.example.expensetracker

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.databinding.ActivityAnalyticsBinding
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*

class AnalyticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyticsBinding
    private lateinit var viewModel: ExpenseViewModel
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        viewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        setupMonthNavigation()
        observeData()
        updateMonthDisplay()
    }

    private fun setupMonthNavigation() {
        binding.btnPreviousMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateMonthDisplay()
        }
        binding.btnNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateMonthDisplay()
        }
    }

    private fun updateMonthDisplay() {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.tvCurrentMonth.text = sdf.format(calendar.time)

        val start = calendar.clone() as Calendar
        start.set(Calendar.DAY_OF_MONTH, 1)
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.SECOND, 0)

        val end = calendar.clone() as Calendar
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH))
        end.set(Calendar.HOUR_OF_DAY, 23)
        end.set(Calendar.MINUTE, 59)
        end.set(Calendar.SECOND, 59)

        viewModel.filterByDateRange(start.timeInMillis, end.timeInMillis)
    }

    private fun observeData() {
        viewModel.filteredExpenses.observe(this) { expenses ->
            updateCharts(expenses)
            updateSummary(expenses)
        }
    }

    private fun updateCharts(expenses: List<ExpenseEntity>) {
        val income = expenses.filter { it.type == "Income" }.sumOf { it.amount }
        val expense = expenses.filter { it.type == "Expense" }.sumOf { it.amount }

        // Bar Chart (Income vs Expense)
        val barEntries = listOf(
            BarEntry(0f, income.toFloat()),
            BarEntry(1f, expense.toFloat())
        )
        val barDataSet = BarDataSet(barEntries, "Income vs Expense")
        barDataSet.colors = listOf(Color.GREEN, Color.RED)
        binding.barChart.data = BarData(barDataSet)
        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Income", "Expense"))
        binding.barChart.xAxis.granularity = 1f
        binding.barChart.xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        binding.barChart.description.isEnabled = false
        binding.barChart.animateY(1000)
        binding.barChart.invalidate()

        // Pie Chart (Expenses by Category)
        val categoryMap = expenses.filter { it.type == "Expense" }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        val pieEntries = categoryMap.map { PieEntry(it.value.toFloat(), it.key) }
        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSet.valueTextSize = 12f

        binding.pieChart.data = PieData(pieDataSet)
        binding.pieChart.description.isEnabled = false
        binding.pieChart.centerText = "Expenses"
        binding.pieChart.animateXY(1000, 1000)
        binding.pieChart.invalidate()
    }

    private fun updateSummary(expenses: List<ExpenseEntity>) {
        val income = expenses.filter { it.type == "Income" }.sumOf { it.amount }
        val expense = expenses.filter { it.type == "Expense" }.sumOf { it.amount }
        val savings = income - expense

        binding.tvTotalIncome.text = String.format("Total Income: ₹%.2f", income)
        binding.tvTotalExpense.text = String.format("Total Expense: ₹%.2f", expense)
        binding.tvNetSavings.text = String.format("Net Savings: ₹%.2f", savings)
        
        binding.tvNetSavings.setTextColor(if (savings >= 0) Color.parseColor("#4CAF50") else Color.RED)
    }
}
