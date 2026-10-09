package com.lebeche.barrioteca.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lebeche.barrioteca.data.Prefs

private enum class AuthTab(val label: String, val icon: ImageVector) {
    Home("Inicio", Icons.Filled.Home),
    Catalog("Catálogo", Icons.AutoMirrored.Filled.LibraryBooks),
    Scan("Escanear", Icons.Filled.QrCodeScanner),
    Settings("Ajustes", Icons.Filled.Settings)
}

private enum class UnauthTab(val label: String, val icon: ImageVector) {
    Login("Entrar", Icons.Filled.Person),
    Catalog("Catálogo", Icons.AutoMirrored.Filled.LibraryBooks),
    About("Proyecto", Icons.Filled.Info)
}

@Composable
fun BarriotecaApp() {
    val context = LocalContext.current
    var member by remember { mutableStateOf(Prefs.member(context)) }
    
    var authTab by remember { mutableStateOf(AuthTab.Home) }
    var unauthTab by remember { mutableStateOf(UnauthTab.Login) }

    // Back del sistema: si no estamos en la sección principal, vuelve a ella
    // (Inicio/Entrar) en lugar de cerrar o enviar a segundo plano la app.
    BackHandler(enabled = member != null && authTab != AuthTab.Home) {
        authTab = AuthTab.Home
    }
    BackHandler(enabled = member == null && unauthTab != UnauthTab.Login) {
        unauthTab = UnauthTab.Login
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ThemeToggle()
            }
        },
        bottomBar = {
            NavigationBar {
                if (member == null) {
                    UnauthTab.entries.forEach { t ->
                        NavigationBarItem(
                            selected = unauthTab == t,
                            onClick = { unauthTab = t },
                            icon = { Icon(t.icon, contentDescription = t.label) },
                            label = { Text(t.label) }
                        )
                    }
                } else {
                    AuthTab.entries.forEach { t ->
                        NavigationBarItem(
                            selected = authTab == t,
                            onClick = { authTab = t },
                            icon = { Icon(t.icon, contentDescription = t.label) },
                            label = { Text(t.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (member == null) {
                when (unauthTab) {
                    UnauthTab.Login -> LoginScreen(onLoggedIn = { m ->
                        member = m
                        authTab = AuthTab.Home
                    })
                    UnauthTab.Catalog -> CatalogScreen(member = null)
                    UnauthTab.About -> AboutScreen()
                }
            } else {
                when (authTab) {
                    AuthTab.Home -> DashboardScreen(member = member!!)
                    AuthTab.Catalog -> CatalogScreen(member = member!!)
                    AuthTab.Scan -> ScanScreen(member = member!!)
                    AuthTab.Settings -> SettingsScreen(
                        member = member!!,
                        onLogout = {
                            Prefs.clearMember(context)
                            member = null
                            unauthTab = UnauthTab.Login
                        }
                    )
                }
            }
        }
    }
}