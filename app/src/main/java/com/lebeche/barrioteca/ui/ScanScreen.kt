package com.lebeche.barrioteca.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lebeche.barrioteca.data.Member
import com.lebeche.barrioteca.data.Prefs
import com.lebeche.barrioteca.data.SlmsApi
import com.lebeche.barrioteca.data.TxLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

@Composable
fun ScanScreen(member: Member) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var action by remember { mutableStateOf("prestamo") }
    var scanning by remember { mutableStateOf(false) }
    var manualCode by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var logs by remember { mutableStateOf(Prefs.logs(context)) }

    fun runAction(raw: String) {
        val code = raw.trim()
        if (code.isEmpty()) return
        scope.launch {
            busy = true
            val res = SlmsApi.performAction(action, code, if (action == "prestamo") member.id else null)
            busy = false
            Prefs.addLog(
                context,
                TxLog(
                    id = System.currentTimeMillis().toString(),
                    timestamp = System.currentTimeMillis(),
                    timeLabel = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
                    action = action,
                    code = code,
                    status = if (res.success) "success" else "error",
                    message = res.message ?: "",
                    user = member.name,
                    bookTitle = res.data?.optString("item_title")
                )
            )
            logs = Prefs.logs(context)
            manualCode = ""
            Toast.makeText(
                context,
                res.message ?: if (res.success) "Operación completada" else "Error",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    if (scanning) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            IconButton(onClick = { scanning = false }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            ScannerCapture(onCode = { code ->
                scanning = false
                runAction(code)
            })
        }
        return
    }

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Préstamo / Devolución",
                style = MaterialTheme.typography.headlineMedium,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionButton("Préstamo", action == "prestamo", Modifier.weight(1f)) { action = "prestamo" }
                ActionButton("Devolución", action == "devolucion", Modifier.weight(1f)) { action = "devolucion" }
            }
        }

        item {
            Button(
                onClick = { scanning = true },
                enabled = !busy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
                Text("  Escanear libro")
            }
        }

        item {
            OutlinedTextField(
                value = manualCode,
                onValueChange = { manualCode = it },
                label = { Text("Código de barras / ISBN") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Button(
                onClick = { runAction(manualCode) },
                enabled = !busy && manualCode.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Aplicar código") }
        }

        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Historial",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(onClick = {
                    Prefs.clearLogs(context)
                    logs = emptyList()
                }) { Text("Borrar") }
            }
        }

        if (logs.isEmpty()) {
            item {
                Text(
                    "Sin operaciones todavía.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(logs, key = { it.id }) { log -> LogRow(log) }
        }
    }
}

@Composable
private fun ActionButton(label: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    if (selected) {
        Button(onClick = onClick, modifier = modifier) { Text(label) }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier) { Text(label) }
    }
}

@Composable
private fun LogRow(log: TxLog) {
    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            "${log.timeLabel} · ${if (log.action == "prestamo") "Préstamo" else "Devolución"}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            buildString {
                append(log.code)
                log.bookTitle?.let { append(" · $it") }
                if (log.status == "error" && log.message.isNotBlank()) append(" · ${log.message}")
            },
            style = MaterialTheme.typography.bodySmall,
            color = if (log.status == "error") MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


