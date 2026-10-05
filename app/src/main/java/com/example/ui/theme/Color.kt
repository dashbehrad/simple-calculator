package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Light Palette
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightOnBackground = Color(0xFF0F172A)
val LightOnSurface = Color(0xFF1E293B)
val LightPrimary = Color(0xFF0284C7) // Sky Blue
val LightOnPrimary = Color(0xFFFFFFFF)
val LightSecondary = Color(0xFFF59E0B) // Amber
val LightOnSecondary = Color(0xFFFFFFFF)
val LightOutline = Color(0xFFE2E8F0)

// Dark Palette
val DarkBackground = Color(0xFF0B1120)
val DarkSurface = Color(0xFF151F32)
val DarkSurfaceVariant = Color(0xFF1E293B)
val DarkOnBackground = Color(0xFFF8FAFC)
val DarkOnSurface = Color(0xFFE2E8F0)
val DarkPrimary = Color(0xFF38BDF8) // Light Sky Blue
val DarkOnPrimary = Color(0xFF0B1120)
val DarkSecondary = Color(0xFFF59E0B) // Amber
val DarkOnSecondary = Color(0xFFFFFFFF)
val DarkOutline = Color(0xFF334155)

@Immutable
data class CalculatorColors(
    val numberBtnBg: Color,
    val numberBtnText: Color,
    val functionBtnBg: Color,
    val functionBtnText: Color,
    val operatorBtnBg: Color,
    val operatorBtnText: Color,
    val equalsBtnBg: Color,
    val equalsBtnText: Color,
    val displayBg: Color,
    val displayText: Color,
    val previewText: Color,
    val historyCardBg: Color
)

val LightCalculatorColors = CalculatorColors(
    numberBtnBg = Color(0xFFFFFFFF),
    numberBtnText = Color(0xFF0F172A),
    functionBtnBg = Color(0xFFE2E8F0),
    functionBtnText = Color(0xFF334155),
    operatorBtnBg = Color(0xFFF59E0B),
    operatorBtnText = Color(0xFFFFFFFF),
    equalsBtnBg = Color(0xFF0284C7),
    equalsBtnText = Color(0xFFFFFFFF),
    displayBg = Color(0xFFF1F5F9),
    displayText = Color(0xFF0F172A),
    previewText = Color(0xFF64748B),
    historyCardBg = Color(0xFFFFFFFF)
)

val DarkCalculatorColors = CalculatorColors(
    numberBtnBg = Color(0xFF1E293B),
    numberBtnText = Color(0xFFF8FAFC),
    functionBtnBg = Color(0xFF334155),
    functionBtnText = Color(0xFFCBD5E1),
    operatorBtnBg = Color(0xFFF59E0B),
    operatorBtnText = Color(0xFFFFFFFF),
    equalsBtnBg = Color(0xFF38BDF8),
    equalsBtnText = Color(0xFF0B1120),
    displayBg = Color(0xFF151F32),
    displayText = Color(0xFFF8FAFC),
    previewText = Color(0xFF94A3B8),
    historyCardBg = Color(0xFF1E293B)
)
