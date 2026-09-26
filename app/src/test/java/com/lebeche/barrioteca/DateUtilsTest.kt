package com.lebeche.barrioteca

import com.lebeche.barrioteca.sync.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateUtilsTest {

    private val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private fun date(s: String): Date = fmt.parse(s)!!

    @Test
    fun daysUntil_positiveForFutureDate() {
        assertEquals(2, DateUtils.daysUntil("2026-03-12", date("2026-03-10")))
    }

    @Test
    fun daysUntil_zeroForToday() {
        assertEquals(0, DateUtils.daysUntil("2026-03-10", date("2026-03-10")))
    }

    @Test
    fun daysUntil_negativeForPastDate() {
        assertEquals(-2, DateUtils.daysUntil("2026-03-08", date("2026-03-10")))
    }

    @Test
    fun daysUntil_blankReturnsNull() {
        assertNull(DateUtils.daysUntil("", Date()))
    }

    @Test
    fun daysUntil_invalidReturnsNull() {
        assertNull(DateUtils.daysUntil("no-es-fecha", Date()))
    }

    @Test
    fun formatDate_returnsSpanishLongForm() {
        assertEquals("5 de febrero", DateUtils.formatDate("2026-02-05"))
    }
}
