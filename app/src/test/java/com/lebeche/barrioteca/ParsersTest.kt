package com.lebeche.barrioteca

import com.lebeche.barrioteca.data.parseBookDetail
import com.lebeche.barrioteca.data.parseCatalog
import com.lebeche.barrioteca.data.parseLoans
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ParsersTest {

    @Test
    fun parseLoans_parsesFieldsAndPrefixesRelativeImage() {
        val arr = JSONArray()
        arr.put(
            JSONObject().apply {
                put("loan_id", "L1")
                put("item_code", "BC-001")
                put("loan_date", "2026-03-01")
                put("due_date", "2026-03-15")
                put("title", "El Quijote")
                put("isbn", "978-84-000-0000-0")
                put("image", "/images/docs/portada.jpg")
            }
        )
        val loans = parseLoans(arr)
        assertEquals(1, loans.size)
        assertEquals("L1", loans[0].loanId)
        assertEquals("https://www.corrientelebeche.es/images/docs/portada.jpg", loans[0].image)
    }

    @Test
    fun parseLoans_emptyOrNullReturnsEmpty() {
        assertTrue(parseLoans(null).isEmpty())
        assertTrue(parseLoans(JSONArray()).isEmpty())
    }

    @Test
    fun parseCatalog_keepsAbsoluteImageUntouched() {
        val arr = JSONArray()
        arr.put(
            JSONObject().apply {
                put("id", "42")
                put("title", "T")
                put("author", "A")
                put("isbn", "978")
                put("status", "disponible")
                put("image", "https://cdn.example.com/x.jpg")
                put("item_code", "BC-042")
            }
        )
        val books = parseCatalog(arr)
        assertEquals(1, books.size)
        assertEquals("https://cdn.example.com/x.jpg", books[0].image)
        assertEquals("BC-042", books[0].itemCode)
    }

    @Test
    fun parseCatalog_appliesDefaultsForMissingFields() {
        val arr = JSONArray()
        arr.put(JSONObject().apply { put("id", "7") })
        val book = parseCatalog(arr).single()
        assertEquals("Autora Desconocida", book.author)
        assertEquals("disponible", book.status)
    }

    @Test
    fun parseBookDetail_nullReturnsNull() {
        assertEquals(null, parseBookDetail(null))
    }

    @Test
    fun parseBookDetail_readsNotesAndPrefixesImage() {
        val data = JSONObject().apply {
            put("id", "1")
            put("title", "T")
            put("notes", "Una sinopsis")
            put("image", "/images/docs/1.jpg")
        }
        val detail = parseBookDetail(data)
        assertEquals("Una sinopsis", detail?.notes)
        assertEquals("https://www.corrientelebeche.es/images/docs/1.jpg", detail?.image)
    }
}
