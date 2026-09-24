package com.lebeche.barrioteca.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Identidad visual de la Barrioteca (misma paleta que la app web).
val BarriotecaCream = Color(0xFFF5F5F0)
val BarriotecaInk = Color(0xFF141414)
val BarriotecaAmber = Color(0xFFE8A33D)

private val LightColors = lightColorScheme(
    primary = Color(0xFF8A5A00),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDEA8),
    onPrimaryContainer = Color(0xFF2A1700),
    secondary = Color(0xFF5F5F5A),
    onSecondary = Color.White,
    background = BarriotecaCream,
    onBackground = BarriotecaInk,
    surface = Color.White,
    onSurface = BarriotecaInk,
    surfaceVariant = Color(0xFFE7E7E2),
    onSurfaceVariant = Color(0xFF47443F),
    outline = Color(0xFF77736C),
    error = Color(0xFFBA1A1A)
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun BarriotecaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
