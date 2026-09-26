package com.lebeche.barrioteca.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Identidad visual de Lebeche (carta de color RGB, recomendada para pantalla).
// Azul madre + ámbar como acento. Mismos tokens que la app web (PWA).
val LebecheTinte = Color(0xFFA9D9ED)       // tinte
val LebecheAzul = Color(0xFF8DCDE7)        // color madre
val LebecheSombra = Color(0xFF3B758B)      // sombra (primary)
val LebecheSombraOscura = Color(0xFF395661) // sombra oscura
val LebecheMasOscuro = Color(0xFF26373E)   // más oscuro (on-container)
val LebecheAmbar = Color(0xFFE8A33D)       // acento
val LebecheCream = Color(0xFFF5F5F0)       // fondo
val LebecheInk = Color(0xFF141414)         // texto

private val LightColors = lightColorScheme(
    primary = LebecheSombra,
    onPrimary = Color.White,
    primaryContainer = LebecheTinte,
    onPrimaryContainer = LebecheMasOscuro,
    secondary = Color(0xFF5F5F5A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE7E7E2),
    onSecondaryContainer = Color(0xFF47443F),
    tertiary = LebecheAmbar,
    onTertiary = LebecheMasOscuro,
    background = LebecheCream,
    onBackground = LebecheInk,
    surface = Color.White,
    onSurface = LebecheInk,
    surfaceVariant = Color(0xFFE7E7E2),
    onSurfaceVariant = Color(0xFF47443F),
    outline = Color(0xFF77736C),
    error = Color(0xFFBA1A1A),
    onError = Color.White
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
