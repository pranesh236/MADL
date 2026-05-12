package com.example.expensetracker.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Futuristic Premium Palette
val AppBg = Color(0xFF0F0F1A)
val CardBg = Color(0xFF1A1A2E)
val CardBgLighter = Color(0xFF1E1E30)
val BorderColor = Color(0xFF2A2A40)

val PrimaryPurple = Color(0xFF6C63FF)
val NeonGreen = Color(0xFF00FF88)
val NeonRed = Color(0xFFFF4757)
val NeonBlue = Color(0xFF4ECCA3)

// Semantic Colors
val IncomeGreen = NeonGreen
val ExpenseRed = NeonRed

// Dashboard Aliases
val Primary = PrimaryPurple
val Secondary = Color.White
val Background = AppBg
val Income = IncomeGreen
val Expense = ExpenseRed
val GradientStart = Color(0xFF6C63FF)
val GradientEnd = Color(0xFF4ECCA3)

// Backward Compatibility Aliases
val DeepPurple = CardBg
val LightPurple = Color(0xFF928DFF)
val DarkBlue = Color(0xFF0F3460)
val PurpleGradient = listOf(PrimaryPurple, NeonBlue)
val BlueGradient = listOf(Color(0xFF0F3460), Color(0xFF16213E))
val PremiumGradient = listOf(CardBg, Color(0xFF16213E), Color(0xFF0F3460))
val GlassWhite = Color(0x1AFFFFFF)
val GlassBorder = Color(0x33FFFFFF)

// Gradient Brushes
val PurpleGradientBrush = Brush.linearGradient(
    colors = listOf(PrimaryPurple, NeonBlue)
)
