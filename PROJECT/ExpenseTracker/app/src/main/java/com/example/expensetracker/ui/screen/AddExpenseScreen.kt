package com.example.expensetracker.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.ui.theme.*
import com.example.expensetracker.ui.viewmodel.ExpenseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    expenseId: Int = -1,
    onBackClick: () -> Unit,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var category by remember { mutableStateOf("Food") }
    var showSuccess by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

    val categories = listOf(
        "Food" to "🍔", "Transport" to "🚗", "Housing" to "🏠",
        "Entertainment" to "🎮", "Health" to "💊", "Shopping" to "🛒",
        "Education" to "📚", "Travel" to "✈️", "Salary" to "💼",
        "Freelance" to "💰", "Gift" to "🎁", "Other" to "📦"
    )

    LaunchedEffect(expenseId) {
        if (expenseId != -1) {
            viewModel.getTransactionById(expenseId)?.let {
                amount = it.amount.toInt().toString()
                note = it.note
                type = it.type
                category = it.category
            }
        }
    }

    if (showSuccess) {
        SuccessAnimation {
            onBackClick()
        }
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(if (expenseId == -1) "Add Transaction" else "Edit", fontWeight = FontWeight.Bold, color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppBg
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Background)
            ) {
                // Type Toggle
                TypeToggle(selectedType = type, onTypeChange = { 
                    type = it
                    category = if (it == TransactionType.INCOME) "Salary" else "Food"
                })

                // Amount Display
                AmountDisplay(amount = amount, type = type, modifier = Modifier.offset(x = offsetX.value.dp))

                // Category Grid - Added weight and fillMaxSize to enable scrolling
                Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 20.dp)) {
                    CategoryGrid(
                        categories = categories,
                        selectedCategory = category,
                        onCategorySelect = { category = it }
                    )
                }

                // Custom Number Pad
                NumberPad(
                    onNumberClick = { 
                        if (it == "." && amount.contains(".")) return@NumberPad
                        if (amount.length < 9) amount += it 
                    },
                    onDeleteClick = { if (amount.isNotEmpty()) amount = amount.dropLast(1) },
                    onDoneClick = {
                        val amountVal = amount.toDoubleOrNull() ?: 0.0
                        if (amountVal > 0) {
                            val transaction = Transaction(
                                id = if (expenseId == -1) 0 else expenseId,
                                amount = amountVal,
                                type = type,
                                category = category,
                                note = note,
                                date = System.currentTimeMillis()
                            )
                            if (expenseId == -1) viewModel.addTransaction(transaction)
                            else viewModel.updateTransaction(transaction)
                            
                            scope.launch {
                                showSuccess = true
                            }
                        } else {
                            scope.launch {
                                repeat(3) {
                                    offsetX.animateTo(20f, tween(50))
                                    offsetX.animateTo(-20f, tween(50))
                                }
                                offsetX.animateTo(0f, tween(50))
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TypeToggle(selectedType: TransactionType, onTypeChange: (TransactionType) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(CardBg)
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(21.dp))
                    .background(if (selectedType == TransactionType.EXPENSE) ExpenseRed else Color.Transparent)
                    .clickable { onTypeChange(TransactionType.EXPENSE) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Expense",
                    color = if (selectedType == TransactionType.EXPENSE) Color.White else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(21.dp))
                    .background(if (selectedType == TransactionType.INCOME) IncomeGreen else Color.Transparent)
                    .clickable { onTypeChange(TransactionType.INCOME) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Income",
                    color = if (selectedType == TransactionType.INCOME) Color.White else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun AmountDisplay(amount: String, type: TransactionType, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Amount", color = Color.Gray, fontSize = 14.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "₹",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (type == TransactionType.INCOME) IncomeGreen else ExpenseRed
            )
            Text(
                text = if (amount.isEmpty()) "0" else amount,
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (type == TransactionType.INCOME) IncomeGreen else ExpenseRed
            )
        }
    }
}

@Composable
fun CategoryGrid(
    categories: List<Pair<String, String>>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(categories) { (name, emoji) ->
            val isSelected = selectedCategory == name
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) PrimaryPurple else CardBg)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) PrimaryPurple else BorderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onCategorySelect(name) }
                    .padding(12.dp)
            ) {
                Text(emoji, fontSize = 24.sp)
                Text(
                    name,
                    fontSize = 10.sp,
                    color = if (isSelected) Color.White else Color.LightGray,
                    maxLines = 1,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun NumberPad(onNumberClick: (String) -> Unit, onDeleteClick: () -> Unit, onDoneClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf(".", "0", "DEL")
        )

        keys.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { key ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(2.8f) // Made more compact
                            .clickable {
                                when (key) {
                                    "DEL" -> onDeleteClick()
                                    else -> onNumberClick(key)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (key == "DEL") {
                            Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = null, tint = AppBg)
                        } else {
                            // Fixed: Using AppBg instead of Secondary (White) to make numbers visible on white background
                            Text(key, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AppBg)
                        }
                    }
                }
            }
        }
        
        Button(
            onClick = onDoneClick,
            modifier = Modifier.fillMaxWidth().height(50.dp).padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save Transaction", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun SuccessAnimation(onAnimationFinished: () -> Unit) {
    var startAnim by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "SuccessScale"
    )

    LaunchedEffect(Unit) {
        startAnim = true
        delay(1500)
        onAnimationFinished()
    }

    Box(modifier = Modifier.fillMaxSize().background(Background), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .clip(CircleShape)
                    .background(IncomeGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(60.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Transaction Saved!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
