package com.example.expensetracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.expensetracker.ui.screen.dashboard.DashboardScreen
import com.example.expensetracker.ui.screen.AddExpenseScreen
import com.example.expensetracker.ui.screen.AnalyticsScreen
import com.example.expensetracker.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerTheme {
                MainNavigation()
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector, val selectedIcon: ImageVector) {
    object Home : Screen("dashboard", "Home", Icons.Outlined.Home, Icons.Filled.Home)
    object Analytics : Screen("analytics", "Analytics", Icons.Outlined.PieChart, Icons.Filled.PieChart)
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            val showBottomBar = currentDestination?.route in listOf("dashboard", "analytics")
            if (showBottomBar) {
                Surface(
                    color = CardBg,
                    modifier = Modifier.drawWithTopGlow()
                ) {
                    BottomAppBar(
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp,
                        actions = {
                            val items = listOf(Screen.Home, Screen.Analytics)
                            items.forEach { screen ->
                                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                IconButton(
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = if (selected) screen.selectedIcon else screen.icon,
                                            contentDescription = screen.label,
                                            tint = if (selected) PrimaryPurple else Color(0xFF666666)
                                        )
                                        if (selected) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(top = 4.dp)
                                                    .size(4.dp)
                                                    .background(PrimaryPurple, CircleShape)
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { navController.navigate("add_transaction/-1") },
                                containerColor = Color.Transparent,
                                contentColor = Color.White,
                                shape = CircleShape,
                                elevation = FloatingActionButtonDefaults.elevation(0.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            Brush.linearGradient(listOf(PrimaryPurple, NeonBlue)),
                                            CircleShape
                                        )
                                        .drawGlow(PrimaryPurple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") {
                DashboardScreen(
                    onTransactionClick = { id -> navController.navigate("add_transaction/$id") }
                )
            }
            composable("analytics") {
                AnalyticsScreen()
            }
            composable(
                route = "add_transaction/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: -1
                AddExpenseScreen(
                    expenseId = id,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

// Helper modifiers for the futuristic look
fun Modifier.drawWithTopGlow() = this.then(
    Modifier.background(
        Brush.verticalGradient(
            colors = listOf(PrimaryPurple.copy(alpha = 0.15f), Color.Transparent),
            startY = 0f,
            endY = 10f
        )
    )
)

fun Modifier.drawGlow(color: Color) = this.then(
    Modifier.padding(4.dp).background(color.copy(alpha = 0.2f), CircleShape)
)
