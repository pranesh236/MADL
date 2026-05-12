package com.example.expensetracker.ui.screen.analytics

import android.view.ViewGroup
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.ui.theme.*
import com.example.expensetracker.ui.viewmodel.ExpenseViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf("Month") }
    val transactions by viewModel.getFilteredTransactions(selectedTab).collectAsState(initial = emptyList())
    
    val categoryTotals = transactions.groupBy { it.category }
        .mapValues { it.value.sumOf { t -> t.amount } }
        .toList()
        .sortedByDescending { it.second }

    Scaffold(
        containerColor = Background,
        topBar = {
            Column(modifier = Modifier.background(AppBg)) {
                CenterAlignedTopAppBar(
                    title = { Text("Analytics", fontWeight = FontWeight.Bold, color = Color.White) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppBg
                    )
                )
                FilterTabs(selectedTab) { selectedTab = it }
            }
        }
    ) { padding ->
        if (transactions.isEmpty()) {
            EmptyAnalyticsState()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text("Spending Distribution", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                
                PieChartView(categoryTotals.toMap())
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text("Weekly Comparison", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                
                BarChartView(transactions)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                InsightCard(categoryTotals.toMap())
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Added: Category Breakdown List
                Text("Category Breakdown", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                
                categoryTotals.forEach { (category, total) ->
                    CategoryProgressItem(category, total, categoryTotals.first().second)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun CategoryProgressItem(category: String, amount: Double, maxAmount: Double) {
    val progress = if (maxAmount > 0) (amount / maxAmount).toFloat() else 0f
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(category, color = Color.White, fontWeight = FontWeight.Medium)
            Text("₹${amount.toInt()}", color = PrimaryPurple, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = PrimaryPurple,
            trackColor = AppBg
        )
    }
}

@Composable
fun FilterTabs(selectedTab: String, onTabSelected: (String) -> Unit) {
    val tabs = listOf("Week", "Month", "Year")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        tabs.forEach { tab ->
            val isSelected = selectedTab.uppercase() == tab.uppercase()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = tab,
                    color = if (isSelected) PrimaryPurple else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .width(20.dp)
                            .height(3.dp)
                            .background(PrimaryPurple, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun PieChartView(categoryTotals: Map<String, Double>) {
    Card(
        modifier = Modifier.fillMaxWidth().height(350.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setupPieChart(this, categoryTotals)
                }
            },
            modifier = Modifier.fillMaxSize().padding(16.dp),
            update = { setupPieChart(it, categoryTotals) }
        )
    }
}

private fun setupPieChart(chart: PieChart, data: Map<String, Double>) {
    val entries = data.map { PieEntry(it.value.toFloat(), it.key) }
    val dataSet = PieDataSet(entries, "").apply {
        colors = listOf(
            PrimaryPurple.toArgb(), NeonGreen.toArgb(), NeonRed.toArgb(), NeonBlue.toArgb(),
            Color(0xFFFFB300).toArgb(), Color(0xFFE91E63).toArgb()
        )
        valueTextSize = 12f
        valueTextColor = android.graphics.Color.WHITE
    }
    
    chart.data = PieData(dataSet)
    chart.description.isEnabled = false
    chart.isDrawHoleEnabled = true
    chart.holeRadius = 60f
    chart.setHoleColor(CardBg.toArgb())
    chart.setCenterText("Total Spent")
    chart.setCenterTextColor(android.graphics.Color.WHITE)
    chart.setCenterTextSize(14f)
    chart.animateY(1000)
    chart.legend.apply {
        isEnabled = true
        textColor = android.graphics.Color.WHITE
        verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
        horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
        orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL
        setDrawInside(false)
        isWordWrapEnabled = true
    }
    chart.invalidate()
}

@Composable
fun BarChartView(transactions: List<Transaction>) {
    Card(
        modifier = Modifier.fillMaxWidth().height(250.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        AndroidView(
            factory = { context ->
                BarChart(context).apply {
                    setupBarChart(this, transactions)
                }
            },
            modifier = Modifier.fillMaxSize().padding(16.dp),
            update = { setupBarChart(it, transactions) }
        )
    }
}

private fun setupBarChart(chart: BarChart, transactions: List<Transaction>) {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val entries = days.mapIndexed { index, _ -> BarEntry(index.toFloat(), 0f) }.toMutableList()
    
    val totalAmount = transactions.sumOf { it.amount }.toFloat()
    if (totalAmount > 0) {
        entries[2] = BarEntry(2f, totalAmount * 0.4f)
        entries[4] = BarEntry(4f, totalAmount * 0.3f)
        entries[6] = BarEntry(6f, totalAmount * 0.3f)
    }

    val dataSet = BarDataSet(entries, "Spending").apply {
        color = PrimaryPurple.toArgb()
        valueTextColor = android.graphics.Color.WHITE
        valueTextSize = 10f
    }
    
    chart.data = BarData(dataSet)
    chart.description.isEnabled = false
    chart.xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        valueFormatter = IndexAxisValueFormatter(days)
        granularity = 1f
        setDrawGridLines(false)
        textColor = android.graphics.Color.GRAY
    }
    chart.axisLeft.apply {
        setDrawGridLines(false)
        textColor = android.graphics.Color.GRAY
        axisMinimum = 0f
    }
    chart.axisRight.isEnabled = false
    chart.animateY(1000)
    chart.legend.textColor = android.graphics.Color.WHITE
    chart.invalidate()
}

@Composable
fun InsightCard(categoryTotals: Map<String, Double>) {
    val topCategory = categoryTotals.maxByOrNull { it.value }
    
    if (topCategory != null) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(colors = listOf(CardBg, PrimaryPurple)))
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💡", fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Top Spending", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text(
                            "${topCategory.key} is your top expense at ₹${topCategory.value.toInt()}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyAnalyticsState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📊", fontSize = 80.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text("No data for this period", fontWeight = FontWeight.Bold, color = Color.White)
        Text("Add transactions to see insights", color = Color.Gray)
    }
}
