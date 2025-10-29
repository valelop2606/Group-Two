package com.example.grouptwo.repository

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.nio.charset.Charset
import com.example.grouptwo.Coctel

// JSON raíz de assets/cocktails.json
@Serializable
data class CocktailsFile(
    val schemaVersion: Int,
    val cocktails: List<Coctel>
)

object CoctelRepositori {

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private var cache: List<Coctel>? = null

    /** Carga todos los cócteles desde assets una sola vez y cachea en memoria */
    fun getAll(context: Context): List<Coctel> {
        cache?.let { return it }
        val text = context.assets.open("cocktails.json")
            .use { it.readBytes().toString(Charset.forName("UTF-8")) }
        val file = json.decodeFromString(CocktailsFile.serializer(), text)
        cache = file.cocktails
        return file.cocktails
    }

    /** Devuelve 1 cóctel por id (o null si no existe) */
    fun getById(context: Context, id: String): Coctel? =
        getAll(context).firstOrNull { it.id == id }

    /** Si cambias el JSON en runtime y quieres recargar */
    fun clearCache() { cache = null }
}

