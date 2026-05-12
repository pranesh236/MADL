package com.example.expensetracker.ui.screen.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.ui.theme.*
import com.example.expensetracker.ui.viewmodel.ExpenseViewModel
import java.util.*

@Composable
fun DashboardScreen(
    onTransactionClick: (Int) -> Unit,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val transactions by viewModel.allTransactions.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val balance by viewModel.totalBalance.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(AppBg)) {
        // Subtle Background Pattern (Grid)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSpacing = 40.dp.toPx()
            for (x in 0..size.width.toInt() step gridSpacing.toInt()) {
                drawLine(Color.White.copy(alpha = 0.03f), Offset(x.toFloat(), 0f), Offset(x.toFloat(), size.height))
            }
            for (y in 0..size.height.toInt() step gridSpacing.toInt()) {
                drawLine(Color.White.copy(alpha = 0.03f), Offset(0f, y.toFloat()), Offset(size.width, y.toFloat()))
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp)
        ) {
            item {
                HeaderSection()
                Spacer(modifier = Modifier.height(24.dp))
                MainBalanceCard(balance, totalIncome, totalExpense)
                Spacer(modifier = Modifier.height(32.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.width(4.dp).height(20.dp).background(PrimaryPurple, RoundedCornerShape(2.dp)))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Recent Transactions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (transactions.isEmpty()) {
                item { EmptyState() }
            } else {
                items(transactions) { transaction ->
                    TransactionCard(transaction) { onTransactionClick(transaction.id) }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 5..11 -> "Good Morning! 👋"
        in 12..16 -> "Good Afternoon! 👋"
        in 17..20 -> "Good Evening! 👋"
        else -> "Good Night! 👋"
    }

    Column {
        Text(text = greeting, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text(text = "Here's your financial summary", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun MainBalanceCard(balance: Double, income: Double, expense: Double) {
    val animatedBalance by animateFloatAsState(
        targetValue = balance.toFloat(),
        animationSpec = tween(1500, easing = FastOutSlowInEasing),
        label = "Balance"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(24.dp, shape = RoundedCornerShape(24.dp), ambientColor = PrimaryPurple, spotColor = PrimaryPurple)
            .border(1.dp, Brush.linearGradient(listOf(PrimaryPurple.copy(0.5f), NeonBlue.copy(0.5f))), RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PurpleGradientBrush)
                .padding(24.dp)
        ) {
            Column {
                Text("Total Balance", color = Color.White.copy(0.7f), fontSize = 14.sp)
                Text(
                    text = String.format(Locale.getDefault(), "₹ %.0f", animatedBalance),
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    BalanceStatItem("Income", income, Icons.Default.ArrowUpward, NeonGreen)
                    BalanceStatItem("Expenses", expense, Icons.Default.ArrowDownward, NeonRed)
                }
            }
        }
    }
}

@Composable
fun BalanceStatItem(label: String, amount: Double, icon: ImageVector, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.White.copy(0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(label, color = Color.White.copy(0.7f), fontSize = 11.sp)
            Text(
                text = String.format(Locale.getDefault(), "₹ %.0f", amount),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.AccountBalanceWallet, null, modifier = Modifier.size(80.dp), tint = PrimaryPurple.copy(0.5f))
        Spacer(modifier = Modifier.height(16.dp))
        Text("No transactions yet!", color = Color.White, fontWeight = FontWeight.Bold)
        Text("Tap + to add your first expense", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun TransactionCard(transaction: Transaction, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgLighter),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (transaction.type == TransactionType.INCOME) NeonGreen.copy(0.15f) else PrimaryPurple.copy(0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(getCategoryEmoji(transaction.category), fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.category, color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    text = java.text.SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(transaction.date)),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Text(
                text = String.format(Locale.getDefault(), "%s₹%.0f", if (transaction.type == TransactionType.INCOME) "+" else "-", transaction.amount),
                color = if (transaction.type == TransactionType.INCOME) NeonGreen else NeonRed,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }
    }
}

fun getCategoryEmoji(category: String): String {
    return when (category) {
        "Food" -> "🍔"
        "Transport" -> "🚗"
        "Housing" -> "🏠"
        "Entertainment" -> "🎮"
        "Health" -> "💊"
        "Shopping" -> "🛒"
        "Education" -> "📚"
        "Travel" -> "✈️"
        "Salary" -> "💼"
        "Freelance" -> "💰"
        "Gift" -> "🎁"
        else -> "📦"
    }
}
