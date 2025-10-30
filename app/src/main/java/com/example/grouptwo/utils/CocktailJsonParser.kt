package com.example.grouptwo.utils

import android.content.Context
import android.util.Log
import com.example.grouptwo.models.CoctelDetalle
import com.example.grouptwo.models.Ingrediente
import com.example.grouptwo.models.Paso
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.long
import java.io.IOException

private const val TAG = "CocktailJsonParser"

/**
 * Serializer flexible: acepta 123, "123", 12.5, true o "ckt_mojito" y lo guarda como String.
 */
object StringOrNumberAsStringSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StringOrNumberAsString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String {
        if (decoder is JsonDecoder) {
            val el: JsonElement = decoder.decodeJsonElement()
            if (el is JsonPrimitive) {
                return when {
                    el.isString -> el.content
                    el.longOrNull != null -> el.long.toString()
                    el.doubleOrNull != null -> el.double.toString()
                    el.booleanOrNull != null -> el.boolean.toString()
                    else -> el.content
                }
            }
        }
        // Fallback si no es JsonDecoder o no fue JsonPrimitive
        return try {
            decoder.decodeString()
        } catch (_: Exception) {
            try { decoder.decodeInt().toString() } catch (_: Exception) { "" }
        }
    }

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }
}

// ================== SCHEMA JSON ==================

@Serializable
data class CocktailsDatabase(
    val schemaVersion: Int = 1,
    val cocktails: List<CocktailJson> = emptyList(),
    val ingredients: List<IngredientJson> = emptyList(),
    val receta_ingredientes: List<RecetaIngredienteJson> = emptyList(),
    val categories: List<String> = emptyList()
)

@Serializable
data class CocktailJson(
    @kotlinx.serialization.Serializable(with = StringOrNumberAsStringSerializer::class)
    val id: String,
    val nombre: String = "",
    val descripcion: String = "",
    val pasos: List<String> = emptyList(),
    val utensilios: List<String> = emptyList(),
    val nivel_dificultad: String = "",
    val nivel_alcohol: String = "",
    val sabor_predominante: String = "",
    val categorias: List<String> = emptyList(),
    val url_video: String = "",
    val es_verificado: Boolean = false,
    val idioma: String = "es"
)

@Serializable
data class IngredientJson(
    @kotlinx.serialization.Serializable(with = StringOrNumberAsStringSerializer::class)
    val id: String,
    val nombre: String = "",
    val tipo: String = "",
    val unidad_medida: String = ""
)

@Serializable
data class RecetaIngredienteJson(
    @kotlinx.serialization.Serializable(with = StringOrNumberAsStringSerializer::class)
    val coctel_id: String,
    @kotlinx.serialization.Serializable(with = StringOrNumberAsStringSerializer::class)
    val ingrediente_id: String,
    val cantidad: Double = 0.0,
    val unidad: String = "",
    val opcional: Boolean = false,
    val notas: String? = null
)

// ================== PARSER ==================

object CocktailJsonParser {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
        explicitNulls = false
    }

    // Caché para evitar leer el JSON múltiples veces
    private var cachedDatabase: CocktailsDatabase? = null

    /**
     * Genera un ID entero estable a partir de un ID String.
     * Si el string es numérico, usa ese número; si no, usa hashCode().
     */
    private fun toStableIntId(idStr: String): Int =
        idStr.toIntOrNull() ?: idStr.hashCode()

    /**
     * Lee y cachea la base de datos completa del JSON desde /assets/cocktails.json
     */
    private fun loadDatabase(context: Context): CocktailsDatabase {
        cachedDatabase?.let { return it }
        return try {
            val jsonString = context.assets.open("cocktails.json")
                .bufferedReader()
                .use { it.readText() }

            val database = json.decodeFromString<CocktailsDatabase>(jsonString)
            cachedDatabase = database
            if (database.cocktails.isEmpty()) {
                Log.w(TAG, "La base cargó pero no hay 'cocktails'. Revisa el contenido del JSON.")
            }
            database
        } catch (e: IOException) {
            Log.e(TAG, "Error al leer cocktails.json (ubicación/nombre del archivo): ${e.message}", e)
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error al parsear JSON (claves/tipos pueden no coincidir): ${e.message}", e)
            throw e
        }
    }

    /**
     * Obtiene los ingredientes de un cóctel específico
     */
    private fun getIngredientesParaCoctel(
        coctelIdStr: String,
        database: CocktailsDatabase
    ): List<Ingrediente> {
        val relaciones = database.receta_ingredientes.filter { it.coctel_id == coctelIdStr }
        return relaciones.mapNotNull { relacion ->
            val ingredienteJson = database.ingredients.find { it.id == relacion.ingrediente_id }
            ingredienteJson?.let {
                Ingrediente(
                    nombre = it.nombre,
                    cantidad = relacion.cantidad,
                    unidad = relacion.unidad,
                    nota = relacion.notas
                )
            }
        }
    }

    /**
     * Convierte los pasos de String a objetos Paso
     */
    private fun convertirPasos(pasos: List<String>): List<Paso> =
        pasos.mapIndexed { index, texto -> Paso(n = index + 1, texto = texto) }

    /**
     * Lee el archivo cocktails.json y retorna lista de CoctelDetalle
     */
    fun loadCocktailsFromAssets(context: Context): List<CoctelDetalle> {
        return try {
            val database = loadDatabase(context)

            database.cocktails.map { c ->
                CoctelDetalle(
                    id = toStableIntId(c.id),
                    nombre = c.nombre,
                    descripcion = c.descripcion,
                    pasos = convertirPasos(c.pasos),
                    ingredientes = getIngredientesParaCoctel(c.id, database),
                    utensilios = c.utensilios,
                    nivelDificultad = c.nivel_dificultad,
                    nivelAlcohol = c.nivel_alcohol,
                    saborPredominante = c.sabor_predominante,
                    categorias = c.categorias,
                    urlVideo = c.url_video
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Fallo loadCocktailsFromAssets, devolviendo lista vacía.", e)
            emptyList()
        }
    }

    /**
     * Busca un cóctel específico por ID (acepta el ID string del JSON o el entero estable)
     */
    fun getCocktailById(context: Context, id: String): CoctelDetalle? {
        return try {
            val database = loadDatabase(context)

            // 1) Intentar por ID exacto del JSON (string)
            database.cocktails.find { it.id == id }?.let { c ->
                return CoctelDetalle(
                    id = toStableIntId(c.id),
                    nombre = c.nombre,
                    descripcion = c.descripcion,
                    pasos = convertirPasos(c.pasos),
                    ingredientes = getIngredientesParaCoctel(c.id, database),
                    utensilios = c.utensilios,
                    nivelDificultad = c.nivel_dificultad,
                    nivelAlcohol = c.nivel_alcohol,
                    saborPredominante = c.sabor_predominante,
                    categorias = c.categorias,
                    urlVideo = c.url_video
                )
            }

            // 2) Intentar si nos pasan un número como string (ID entero estable)
            val maybeInt = id.toIntOrNull()
            if (maybeInt != null) {
                val c = database.cocktails.find { toStableIntId(it.id) == maybeInt }
                if (c != null) {
                    return CoctelDetalle(
                        id = toStableIntId(c.id),
                        nombre = c.nombre,
                        descripcion = c.descripcion,
                        pasos = convertirPasos(c.pasos),
                        ingredientes = getIngredientesParaCoctel(c.id, database),
                        utensilios = c.utensilios,
                        nivelDificultad = c.nivel_dificultad,
                        nivelAlcohol = c.nivel_alcohol,
                        saborPredominante = c.sabor_predominante,
                        categorias = c.categorias,
                        urlVideo = c.url_video
                    )
                }
            }
            null
        } catch (e: Exception) {
            Log.e(TAG, "Fallo getCocktailById($id).", e)
            null
        }
    }

    /**
     * Búsqueda por nombre/descripcion (case-insensitive). Si query está en blanco, devuelve todo.
     */
    fun searchCocktails(context: Context, query: String): List<CoctelDetalle> {
        if (query.isBlank()) return loadCocktailsFromAssets(context)
        return loadCocktailsFromAssets(context).filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    (it.descripcion?.contains(query, ignoreCase = true) == true)
        }
    }

    /**
     * Filtra cócteles por categoría
     */
    fun getCocktailsByCategory(context: Context, category: String): List<CoctelDetalle> {
        return loadCocktailsFromAssets(context).filter { cocktail ->
            cocktail.categorias?.any { it.equals(category, ignoreCase = true) } == true
        }
    }

    /**
     * Obtiene la lista de todas las categorías disponibles
     */
    fun getAllCategories(context: Context): List<String> {
        return try {
            val database = loadDatabase(context)
            database.categories
        } catch (e: Exception) {
            Log.e(TAG, "Fallo getAllCategories().", e)
            emptyList()
        }
    }

    /**
     * Filtra cócteles por nivel de dificultad
     */
    fun getCocktailsByDifficulty(context: Context, difficulty: String): List<CoctelDetalle> {
        return loadCocktailsFromAssets(context).filter {
            it.nivelDificultad?.equals(difficulty, ignoreCase = true) == true
        }
    }

    /**
     * Filtra cócteles por nivel de alcohol
     */
    fun getCocktailsByAlcoholLevel(context: Context, alcoholLevel: String): List<CoctelDetalle> {
        return loadCocktailsFromAssets(context).filter {
            it.nivelAlcohol?.equals(alcoholLevel, ignoreCase = true) == true
        }
    }

    /**
     * Filtra cócteles por sabor predominante
     */
    fun getCocktailsByFlavor(context: Context, flavor: String): List<CoctelDetalle> {
        return loadCocktailsFromAssets(context).filter {
            it.saborPredominante?.equals(flavor, ignoreCase = true) == true
        }
    }

    /**
     * Limpia el caché (útil si actualizas el JSON)
     */
    fun clearCache() {
        cachedDatabase = null
    }
}

/*
ProGuard/R8 (build release), por si usas ofuscación:

-keep class kotlinx.serialization.** { *; }
-keepclassmembers class **$$serializer { *; }
-keep @kotlinx.serialization.Serializable class ** { *; }
-keep class com.example.grouptwo.utils.** { *; }
*/
