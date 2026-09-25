package com.lebeche.barrioteca.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            "Sobre el proyecto",
            style = MaterialTheme.typography.headlineMedium,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(Modifier.height(24.dp))

        Text(
            "La Barrioteca Acalencá es una iniciativa para fomentar la lectura y el intercambio cultural. Nuestro objetivo es crear una biblioteca autogestionada, accesible para todas las vecinas y vecinos.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            "A través de esta aplicación puedes consultar nuestro catálogo libremente. Además, si eres socia, puedes identificarte para pedir libros prestados y gestionar tus devoluciones cómodamente desde tu teléfono.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.corrientelebeche.es"))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
            Text("  Visitar nuestra web")
        }
    }
}
