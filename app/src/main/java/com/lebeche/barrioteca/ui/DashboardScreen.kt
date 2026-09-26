package com.lebeche.barrioteca.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lebeche.barrioteca.data.Loan
import com.lebeche.barrioteca.data.Member
import com.lebeche.barrioteca.data.RefreshSignal
import com.lebeche.barrioteca.data.SlmsApi
import com.lebeche.barrioteca.data.parseLoans
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DashboardScreen(member: Member) {
    var loans by remember { mutableStateOf<List<Loan>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(member.id, RefreshSignal.loansVersion) {
        loading = true
        val res = SlmsApi.memberLoans(member.id)
        loans = if (res.success) parseLoans(res.list) else emptyList()
        loading = false
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "Bienvenida",
            style = MaterialTheme.typography.headlineMedium,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Hola, ${member.name}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                member.expireDate?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Carné de socia caduca el ${formatDate(it)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (member.isExpired) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Tu carné ha caducado. Pásate por la biblioteca para renovarlo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            "Libros en préstamo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        when {
            loading -> {
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            loans.isEmpty() -> {
                Text(
                    "No tienes libros prestados actualmente.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            else -> loans.forEach { loan -> LoanCard(loan) }
        }
    }
}

@Composable
private fun LoanCard(loan: Loan) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    loan.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Vence: ${formatDate(loan.dueDate)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            if (loan.itemCode.isNotBlank()) {
                Text(
                    "Ejemplar: ${loan.itemCode}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatDate(dateStr: String): String {
    if (dateStr.isBlank()) return "—"
    return runCatching {
        val d = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateStr.trim()) ?: return dateStr
        SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-ES")).format(d)
    }.getOrDefault(dateStr)
}
