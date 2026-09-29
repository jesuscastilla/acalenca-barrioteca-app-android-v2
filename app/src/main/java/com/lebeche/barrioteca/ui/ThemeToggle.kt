package com.lebeche.barrioteca.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.lebeche.barrioteca.data.Prefs

@Composable
fun ThemeToggle() {
    val context = LocalContext.current
    IconButton(onClick = {
        val nextMode = (ThemeState.themeMode + 1) % 3
        ThemeState.themeMode = nextMode
        Prefs.setThemeMode(context, nextMode)
    }) {
        val icon = when (ThemeState.themeMode) {
            1 -> Icons.Filled.DarkMode
            2 -> Icons.Filled.LightMode
            else -> Icons.Filled.SettingsBrightness
        }
        Icon(
            imageVector = icon,
            contentDescription = "Cambiar tema",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
