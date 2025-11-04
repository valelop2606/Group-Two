package com.example.grouptwo.data

import android.content.Context
import com.example.grouptwo.dataclases.CoctelesDatabase
import com.example.grouptwo.dataclases.Coctel
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate

object GuardarMiReceta {
    private const val FILE_NAME = "cocktails.json"
    private val json = Json { prettyPrint = true }

    fun addRecipe(context: Context, coctel: Coctel) {
        val data = read(context)                     // CoctelesDatabase(schemaVersion, cocteles: List)
        val list = data.cocteles.toMutableList()     // trabajar sobre copia mutable

        val idx = list.indexOfFirst { it.id == coctel.id }
        if (idx >= 0) list[idx] = coctel else list.add(coctel)

        write(context, CoctelesDatabase(schemaVersion = data.schemaVersion, cocteles = list.toList()))
    }

    fun generarIdDesdeNombre(nombre: String): String =
        "ckt_" + nombre.lowercase()
            .replace(Regex("[^a-z0-9\\s_]"), "")
            .trim()
            .replace("\\s+".toRegex(), "_")

    fun hoyISO(): String = LocalDate.now().toString()

    // ---- privados mínimos ----
    private fun read(context: Context): CoctelesDatabase {
        val f = File(context.filesDir, FILE_NAME)
        if (!f.exists()) return CoctelesDatabase(schemaVersion = 1, cocteles = emptyList())
        return runCatching { json.decodeFromString(CoctelesDatabase.serializer(), f.readText()) }
            .getOrElse { CoctelesDatabase(schemaVersion = 1, cocteles = emptyList()) }
    }

    private fun write(context: Context, data: CoctelesDatabase) {
        File(context.filesDir, FILE_NAME)
            .writeText(json.encodeToString(CoctelesDatabase.serializer(), data))
    }
    fun getAll(context: Context) = read(context).cocteles

}
