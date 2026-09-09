package com.lebeche.barrioteca.data

import org.json.JSONArray

/** Convierte la respuesta `member-loans` (array JSON) en una lista de préstamos. */
fun parseLoans(list: JSONArray?): List<Loan> {
    if (list == null) return emptyList()
    return buildList {
        for (i in 0 until list.length()) {
            val j = list.optJSONObject(i) ?: continue
            add(
                Loan(
                    loanId = j.optString("loan_id"),
                    itemCode = j.optString("item_code"),
                    loanDate = j.optString("loan_date"),
                    dueDate = j.optString("due_date"),
                    title = j.optString("title", "Título no disponible"),
                    isbn = j.optString("isbn"),
                    image = j.optString("image")
                )
            )
        }
    }
}

/** Convierte la respuesta `catalog-list` (array JSON) en una lista de libros. */
fun parseCatalog(list: JSONArray?): List<CatalogBook> {
    if (list == null) return emptyList()
    return buildList {
        for (i in 0 until list.length()) {
            val j = list.optJSONObject(i) ?: continue
            add(
                CatalogBook(
                    id = j.optString("id"),
                    title = j.optString("title", "Sin título"),
                    author = j.optString("author", "Autora Desconocida"),
                    isbn = j.optString("isbn"),
                    status = j.optString("status", "disponible"),
                    image = j.optString("image"),
                    notes = j.optString("notes"),
                    itemCode = j.optString("item_code")
                )
            )
        }
    }
}
