package com.lebeche.barrioteca.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Señal ligera para invalidar el estado cacheado en memoria (catálogo y
 * préstamos) tras una operación de préstamo/devolución. Usa Compose snapshot
 * state: al incrementar el contador, los `LaunchedEffect` que lo leen se
 * vuelven a ejecutar y recargan los datos desde SLiMS.
 */
object RefreshSignal {
    var catalogVersion by mutableStateOf(0)
        private set
    var loansVersion by mutableStateOf(0)
        private set

    fun bumpCatalog() { catalogVersion++ }
    fun bumpLoans() { loansVersion++ }
}
