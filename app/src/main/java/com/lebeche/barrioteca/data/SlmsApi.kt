package com.lebeche.barrioteca.data

import com.lebeche.barrioteca.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Cliente de la API de la Barrioteca.
 *
 * Habla con el mismo proxy PHP (`api-proxy.php`) que usa la app web React,
 * reutilizando las acciones: verify-member, member-loans, catalog-list y
 * perform-action. La URL base se inyecta desde BuildConfig (definida en
 * app/build.gradle) para no dispersarla por el código.
 */
object SlmsApi {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    data class ApiResult(
        val success: Boolean,
        val data: JSONObject?,
        val list: JSONArray?,
        val message: String?
    )

    private suspend fun get(action: String, params: Map<String, String>): ApiResult =
        withContext(Dispatchers.IO) {
            val url = BuildConfig.API_BASE_URL.toHttpUrl().newBuilder().apply {
                addQueryParameter("action", action)
                params.forEach { (k, v) -> addQueryParameter(k, v) }
            }.build()
            val req = Request.Builder().url(url).get().build()
            runCatching {
                client.newCall(req).execute().use { resp ->
                    parse(resp.body?.string().orEmpty())
                }
            }.getOrElse { ApiResult(false, null, null, it.message ?: "Error de red") }
        }

    private suspend fun post(action: String, body: JSONObject): ApiResult =
        withContext(Dispatchers.IO) {
            val url = BuildConfig.API_BASE_URL.toHttpUrl().newBuilder()
                .addQueryParameter("action", action)
                .build()
            val media = "application/json; charset=utf-8".toMediaType()
            val req = Request.Builder().url(url)
                .post(body.toString().toRequestBody(media))
                .header("Accept", "application/json")
                .build()
            runCatching {
                client.newCall(req).execute().use { resp ->
                    parse(resp.body?.string().orEmpty())
                }
            }.getOrElse { ApiResult(false, null, null, it.message ?: "Error de red") }
        }

    private fun parse(text: String): ApiResult {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return ApiResult(false, null, null, "Respuesta vacía")

        // Algunas acciones (catalog-list, catalog-proxy) devuelven un array plano.
        if (trimmed.startsWith("[")) {
            return runCatching { ApiResult(true, null, JSONArray(trimmed), null) }
                .getOrElse { ApiResult(false, null, null, "Respuesta no válida") }
        }

        return runCatching {
            val json = JSONObject(trimmed)
            val status = json.optString("status", "success")
            val message = if (json.has("message")) json.optString("message") else null
            val success = status == "success"
            val data: Any? = if (json.has("data") && !json.isNull("data")) json.opt("data") else null
            ApiResult(
                success = success,
                data = data as? JSONObject,
                list = data as? JSONArray,
                message = message
            )
        }.getOrElse { ApiResult(false, null, null, "Respuesta no válida del servidor") }
    }

    private fun String.toHttpUrl(): HttpUrl =
        toHttpUrlOrNull()
            ?: BuildConfig.API_BASE_URL.toHttpUrlOrNull()!!

    // ── Acciones ────────────────────────────────────────────────────
    suspend fun verifyMember(memberId: String): ApiResult =
        get("verify-member", mapOf("member_id" to memberId))

    suspend fun memberLoans(memberId: String): ApiResult =
        get("member-loans", mapOf("member_id" to memberId))

    suspend fun catalogList(): ApiResult =
        get("catalog-list", emptyMap())

    suspend fun performAction(action: String, code: String, memberId: String?): ApiResult {
        val body = JSONObject().apply {
            put("accion", action)
            put("code", code)
            if (memberId != null) put("member_id", memberId)
        }
        return post("perform-action", body)
    }
}
