package com.lebeche.barrioteca.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lebeche.barrioteca.data.Member
import com.lebeche.barrioteca.data.Prefs
import com.lebeche.barrioteca.data.SlmsApi
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoggedIn: (Member) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var manualId by remember { mutableStateOf("") }
    var scanning by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun doLogin(rawId: String) {
        val id = rawId.trim()
        if (id.isEmpty()) return
        scope.launch {
            loading = true
            error = null
            val res = SlmsApi.verifyMember(id)
            loading = false
            if (res.success && res.data != null) {
                val member = Member(
                    id = id,
                    name = res.data.optString("member_name", "Socia $id"),
                    expireDate = if (res.data.isNull("expire_date")) null
                        else res.data.optString("expire_date"),
                    isExpired = res.data.optBoolean("is_expired", false)
                )
                Prefs.saveMember(context, member)
                onLoggedIn(member)
            } else {
                error = res.message ?: "No se pudo verificar esta socia."
            }
        }
    }

    if (scanning) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            IconButton(onClick = { scanning = false }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            ScannerCapture(onCode = { code ->
                scanning = false
                doLogin(code)
            })
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(48.dp))

        Text(
            "Barrioteca Acalencá",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Identifícate para gestionar tus préstamos.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = manualId,
            onValueChange = { manualId = it },
            label = { Text("ID de socia") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { doLogin(manualId) },
            enabled = !loading && manualId.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loading) CircularProgressIndicator(
                modifier = Modifier.height(20.dp),
                strokeWidth = 2.dp
            ) else Text("Entrar")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "o",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { scanning = true },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
            Spacer(Modifier.height(0.dp))
            Text("  Escanear carné (QR o código de barras)")
        }

        if (error != null) {
            Spacer(Modifier.height(16.dp))
            Text(
                error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(48.dp))
    }
}
