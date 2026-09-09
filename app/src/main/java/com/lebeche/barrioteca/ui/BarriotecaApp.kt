package com.lebeche.barrioteca.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Home
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
import com.lebeche.barrioteca.data.Member
import com.lebeche.barrioteca.data.Prefs

private enum class Tab(val label: String, val icon: ImageVector) {
    Home("Inicio", Icons.Filled.Home),
    Catalog("Catálogo", Icons.AutoMirrored.Filled.LibraryBooks),
    Scan("Escanear", Icons.Filled.QrCodeScanner),
    Settings("Ajustes", Icons.Filled.Settings)
}

@Composable
fun BarriotecaApp() {
    val context = LocalContext.current
    var member by remember { mutableStateOf(Prefs.member(context)) }
    var tab by remember { mutableStateOf(Tab.Home) }

    if (member == null) {
        LoginScreen(onLoggedIn = { m ->
            member = m
            tab = Tab.Home
        })
        return
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                Tab.entries.forEach { t ->
                    NavigationBarItem(
                        selected = tab == t,
                        onClick = { tab = t },
                        icon = { Icon(t.icon, contentDescription = t.label) },
                        label = { Text(t.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (tab) {
                Tab.Home -> DashboardScreen(member = member!!)
                Tab.Catalog -> CatalogScreen(member = member!!)
                Tab.Scan -> ScanScreen(member = member!!)
                Tab.Settings -> SettingsScreen(
                    member = member!!,
                    onLogout = {
                        Prefs.clearMember(context)
                        member = null
                    }
                )
            }
        }
    }
}
