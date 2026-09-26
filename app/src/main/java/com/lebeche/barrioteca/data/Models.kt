package com.lebeche.barrioteca.data

/** Socia conectada (login sin contraseña: solo el ID del carné). */
data class Member(
    val id: String,
    val name: String,
    val expireDate: String? = null,
    val isExpired: Boolean = false
)

/** Préstamo activo de una socia, tal como lo devuelve SLiMS. */
data class Loan(
    val loanId: String,
    val itemCode: String,
    val loanDate: String,
    val dueDate: String,
    val title: String,
    val isbn: String,
    val image: String
)

/** Libro del catálogo (acción `catalog-list` del proxy). */
data class CatalogBook(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val status: String,     // "disponible" | "prestada"
    val image: String,
    val notes: String,
    val itemCode: String
)

/** Registro local de una operación de préstamo o devolución. */
data class TxLog(
    val id: String,
    val timestamp: Long,
    val timeLabel: String,
    val action: String,     // "prestamo" | "devolucion"
    val code: String,
    val status: String,     // "success" | "error"
    val message: String,
    val user: String?,
    val bookTitle: String?
)

/** Detalle de un título (sinopsis completa) devuelto por `action=book-detail`. */
data class BookDetail(
    val id: String,
    val title: String,
    val notes: String,
    val image: String
)
