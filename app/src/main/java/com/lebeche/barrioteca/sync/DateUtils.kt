package com.lebeche.barrioteca.sync

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Utilidades de fechas compartidas por la sincronización y las notificaciones.
 * Se extraen de `SyncEngine` para poder testearlas sin depender del reloj real.
 */
object DateUtils {

    /**
     * Días entre `today` y `dateStr` (formato `yyyy-MM-dd`).
     * Valor negativo = fecha pasada. Devuelve `null` si la fecha es inválida.
     */
    fun daysUntil(dateStr: String, today: Date = Date()): Int? {
        if (dateStr.isBlank()) return null
        return runCatching {
            val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val target = fmt.parse(dateStr.trim()) ?: return null
            val todayStart = Calendar.getInstance().apply {
                time = today
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
            TimeUnit.DAYS.convert(target.time - todayStart.time, TimeUnit.MILLISECONDS).toInt()
        }.getOrNull()
    }

    /** Formatea `yyyy-MM-dd` como `"d de MMMM"` en español. */
    fun formatDate(dateStr: String): String {
        return runCatching {
            val d = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateStr.trim()) ?: return dateStr
            SimpleDateFormat("d 'de' MMMM", Locale.forLanguageTag("es-ES")).format(d)
        }.getOrDefault(dateStr)
    }
}
