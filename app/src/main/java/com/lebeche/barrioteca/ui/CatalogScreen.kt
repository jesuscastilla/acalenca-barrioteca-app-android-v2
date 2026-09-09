package com.lebeche.barrioteca.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lebeche.barrioteca.data.CatalogBook
import com.lebeche.barrioteca.data.Member
import com.lebeche.barrioteca.data.SlmsApi
import com.lebeche.barrioteca.data.parseCatalog

@Composable
fun CatalogScreen(member: Member) {
    var books by remember { mutableStateOf<List<CatalogBook>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<CatalogBook?>(null) }

    LaunchedEffect(Unit) {
        loading = true
        val res = SlmsApi.catalogList()
        books = if (res.success) parseCatalog(res.list) else emptyList()
        loading = false
    }

    val filtered = if (query.isBlank()) books else books.filter { b ->
        b.title.contains(query, ignoreCase = true) ||
            b.author.contains(query, ignoreCase = true) ||
            b.isbn.contains(query.trim()) ||
            b.itemCode.contains(query.trim())
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Catálogo de la Biblioteca",
            style = MaterialTheme.typography.headlineMedium,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Busca por título, autora o ISBN") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "${filtered.size} de ${books.size}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))

        when {
            loading -> Column(
                Modifier.fillMaxWidth().padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) { CircularProgressIndicator() }
            filtered.isEmpty() -> Text(
                "El catálogo está vacío.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered, key = { it.id.ifBlank { "${it.title}-${it.itemCode}" } }) { book ->
                    BookRow(book) { selected = book }
                }
            }
        }
    }

    selected?.let { book ->
        BookDetailDialog(
            book = book,
            member = member,
            onDismiss = { selected = null }
        )
    }
}

@Composable
private fun BookRow(book: CatalogBook, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (book.image.isNotBlank()) {
                AsyncImage(
                    model = book.image,
                    contentDescription = book.title,
                    modifier = Modifier
                        .size(width = 56.dp, height = 80.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
                Spacer(Modifier.size(12.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(
                    book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    if (book.status == "disponible") "Disponible" else "Prestada",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (book.status == "disponible")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
