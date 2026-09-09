package com.lebeche.barrioteca.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Persistencia local en SharedPreferences: socia activa, historial de operaciones
 * y las "instantáneas" que usa la sincronización para detectar cambios.
 */
object Prefs {
    private const val FILE = "barrioteca_prefs"

    private fun sp(ctx: Context) = ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    // ── Socia activa ──────────────────────────────────────────────
    fun saveMember(ctx: Context, m: Member) {
        val json = JSONObject().apply {
            put("id", m.id)
            put("name", m.name)
            put("expire_date", m.expireDate ?: JSONObject.NULL)
            put("is_expired", m.isExpired)
        }
        sp(ctx).edit().putString("member", json.toString()).apply()
    }

    fun member(ctx: Context): Member? {
        val raw = sp(ctx).getString("member", null) ?: return null
        return runCatching {
            val j = JSONObject(raw)
            Member(
                id = j.getString("id"),
                name = j.getString("name"),
                expireDate = if (j.isNull("expire_date")) null else j.optString("expire_date"),
                isExpired = j.optBoolean("is_expired", false)
            )
        }.getOrNull()
    }

    fun clearMember(ctx: Context) {
        sp(ctx).edit().remove("member").apply()
    }

    // ── Historial de operaciones ──────────────────────────────────
    fun logs(ctx: Context): List<TxLog> {
        val raw = sp(ctx).getString("logs", null) ?: return emptyList()
        return runCatching {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { i ->
                val j = arr.getJSONObject(i)
                TxLog(
                    id = j.getString("id"),
                    timestamp = j.getLong("timestamp"),
                    timeLabel = j.getString("time_label"),
                    action = j.getString("action"),
                    code = j.getString("code"),
                    status = j.getString("status"),
                    message = j.optString("message"),
                    user = if (j.isNull("user")) null else j.optString("user"),
                    bookTitle = if (j.isNull("book_title")) null else j.optString("book_title")
                )
            }
        }.getOrNull() ?: emptyList()
    }

    fun addLog(ctx: Context, log: TxLog) {
        val all = logs(ctx).toMutableList()
        all.add(0, log)
        if (all.size > 100) all.subList(100, all.size).clear()
        sp(ctx).edit().putString("logs", logsToJson(all).toString()).apply()
    }

    fun clearLogs(ctx: Context) {
        sp(ctx).edit().remove("logs").apply()
    }

    private fun logsToJson(list: List<TxLog>): JSONArray = JSONArray().apply {
        list.forEach { l ->
            put(JSONObject().apply {
                put("id", l.id)
                put("timestamp", l.timestamp)
                put("time_label", l.timeLabel)
                put("action", l.action)
                put("code", l.code)
                put("status", l.status)
                put("message", l.message)
                put("user", l.user ?: JSONObject.NULL)
                put("book_title", l.bookTitle ?: JSONObject.NULL)
            })
        }
    }

    // ── Instantánea de préstamos (para detectar cambios) ──────────
    fun saveLoansSnapshot(ctx: Context, loans: List<Loan>) {
        val arr = JSONArray().apply {
            loans.forEach { l ->
                put(JSONObject().apply {
                    put("loan_id", l.loanId)
                    put("item_code", l.itemCode)
                    put("loan_date", l.loanDate)
                    put("due_date", l.dueDate)
                    put("title", l.title)
                    put("isbn", l.isbn)
                    put("image", l.image)
                })
            }
        }
        sp(ctx).edit().putString("loans_snapshot", arr.toString()).apply()
    }

    fun loansSnapshot(ctx: Context): List<Loan> {
        val raw = sp(ctx).getString("loans_snapshot", null) ?: return emptyList()
        return runCatching {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { i ->
                val j = arr.getJSONObject(i)
                Loan(
                    loanId = j.getString("loan_id"),
                    itemCode = j.optString("item_code"),
                    loanDate = j.optString("loan_date"),
                    dueDate = j.optString("due_date"),
                    title = j.optString("title"),
                    isbn = j.optString("isbn"),
                    image = j.optString("image")
                )
            }
        }.getOrNull() ?: emptyList()
    }

    // ── Instantánea del catálogo (para detectar libros nuevos) ────
    fun saveCatalogIds(ctx: Context, ids: Set<String>) {
        sp(ctx).edit().putStringSet("catalog_ids", ids).apply()
    }

    fun catalogIds(ctx: Context): Set<String> =
        sp(ctx).getStringSet("catalog_ids", emptySet()) ?: emptySet()

    // ── Vencimientos ya notificados (evitar spam) ─────────────────
    fun notifiedDue(ctx: Context, loanId: String): String? =
        sp(ctx).getString("due_$loanId", null)

    fun markNotifiedDue(ctx: Context, loanId: String, dueDate: String) {
        sp(ctx).edit().putString("due_$loanId", dueDate).apply()
    }

    fun notifiedMembership(ctx: Context, expireDate: String): Boolean =
        sp(ctx).getBoolean("member_$expireDate", false)

    fun markNotifiedMembership(ctx: Context, expireDate: String) {
        sp(ctx).edit().putBoolean("member_$expireDate", true).apply()
    }
}
