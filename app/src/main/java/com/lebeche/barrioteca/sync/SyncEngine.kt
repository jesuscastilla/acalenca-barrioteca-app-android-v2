package com.lebeche.barrioteca.sync

import android.content.Context
import com.lebeche.barrioteca.data.Member
import com.lebeche.barrioteca.data.Prefs
import com.lebeche.barrioteca.data.SlmsApi
import com.lebeche.barrioteca.data.parseLoans
import com.lebeche.barrioteca.notif.Notifications

/**
 * Compara el estado de SLiMS con la última instantánea guardada y notifica
 * los cambios relacionados con la socia conectada:
 *  - caducidad de la membresía,
 *  - préstamos nuevos y devoluciones,
 *  - préstamos próximos a vencer,
 *  - libros nuevos añadidos al catálogo.
 */
object SyncEngine {

    suspend fun sync(context: Context) {
        val member = Prefs.member(context) ?: return

        runCatching { syncMembership(context, member) }
        runCatching { syncLoans(context, member.id) }
        runCatching { syncCatalog(context) }
    }

    private suspend fun syncMembership(context: Context, old: Member) {
        val res = SlmsApi.verifyMember(old.id)
        if (!res.success || res.data == null) return

        val name = res.data.optString("member_name", old.name)
        val expire = if (res.data.isNull("expire_date")) null else res.data.optString("expire_date")
        val expired = res.data.optBoolean("is_expired", false)
        Prefs.saveMember(context, Member(old.id, name, expire, expired))

        if (expired) {
            if (!Prefs.notifiedMembership(context, "expired")) {
                Notifications.notify(
                    context,
                    "Membresía caducada",
                    "Tu carné de socia ha caducado. Pásate por la biblioteca para renovarlo.",
                    2001
                )
                Prefs.markNotifiedMembership(context, "expired")
            }
        } else if (expire != null) {
            val days = DateUtils.daysUntil(expire)
            if (days != null && days in 0..7 && !Prefs.notifiedMembership(context, expire)) {
                val msg = if (days == 0) "Tu carné de socia caduca hoy."
                else "Tu carné de socia caduca en $days día(s)."
                Notifications.notify(context, "Caducidad de socia", msg, 2001)
                Prefs.markNotifiedMembership(context, expire)
            }
        }
    }

    private suspend fun syncLoans(context: Context, memberId: String) {
        val res = SlmsApi.memberLoans(memberId)
        if (!res.success) return

        val loans = parseLoans(res.list)
        val previous = Prefs.loansSnapshot(context)
        val prevIds = previous.map { it.loanId }.toSet()
        val currIds = loans.map { it.loanId }.toSet()

        // Préstamos nuevos
        for (l in loans.filter { it.loanId !in prevIds }) {
            Notifications.notify(
                context,
                "Nuevo préstamo",
                "${l.title} — vence el ${DateUtils.formatDate(l.dueDate)}.",
                idFor(l.loanId)
            )
        }

        // Devoluciones (ya no están en el listado)
        for (l in previous.filter { it.loanId !in currIds }) {
            Notifications.notify(context, "Libro devuelto", l.title, idFor(l.loanId))
        }

        // Préstamos próximos a vencer (una sola vez por préstamo y fecha)
        for (l in loans) {
            val days = DateUtils.daysUntil(l.dueDate) ?: continue
            if (days in -2..2) {
                if (Prefs.notifiedDue(context, l.loanId) != l.dueDate) {
                    val msg = when {
                        days < 0 -> "${l.title} — préstamo vencido."
                        days == 0 -> "${l.title} — vence hoy."
                        else -> "${l.title} — vence en $days día(s)."
                    }
                    Notifications.notify(context, "Préstamo próximo a vencer", msg, idFor(l.loanId))
                    Prefs.markNotifiedDue(context, l.loanId, l.dueDate)
                }
            }
        }

        Prefs.saveLoansSnapshot(context, loans)
    }

    private suspend fun syncCatalog(context: Context) {
        val res = SlmsApi.catalogList()
        if (!res.success || res.list == null) return

        val ids = linkedSetOf<String>()
        for (i in 0 until res.list.length()) {
            val id = res.list.optJSONObject(i)?.optString("id").orEmpty()
            if (id.isNotEmpty()) ids.add(id)
        }

        val previous = Prefs.catalogIds(context)
        val added = ids - previous
        if (previous.isNotEmpty() && added.isNotEmpty()) {
            val msg = if (added.size == 1) "Se ha añadido 1 libro nuevo."
            else "Se han añadido ${added.size} libros nuevos."
            Notifications.notify(context, "Novedades en la biblioteca", msg, 2002)
        }
        Prefs.saveCatalogIds(context, ids)
    }

    private fun idFor(loanId: String): Int = (loanId.hashCode() and 0x7fffff) + 3000
}
