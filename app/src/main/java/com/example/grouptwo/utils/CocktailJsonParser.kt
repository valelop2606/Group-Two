package com.example.grouptwo.utils

import android.content.Context
import com.example.grouptwo.models.CoctelDetalle
import com.example.grouptwo.models.Ingrediente
import com.example.grouptwo.models.Paso
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

// Estructura exacta de tu JSON
@Serializable
data class CocktailsDatabase(
    val schemaVersion: Int,
    val cocktails: List<CocktailJson>,
    val ingredients: List<IngredientJson>,
    val receta_ingredientes: List<RecetaIngredienteJson>,
    val categories: List<String>
)

@Serializable
data class CocktailJson(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val pasos: List<String>,
    val utensilios: List<String>,
    val nivel_dificultad: String,
    val nivel_alcohol: String,
    val sabor_predominante: String,
    val categorias: List<String>,
    val url_video: String,
    val es_verificado: Boolean,
    val idioma: String
)

@Serializable
data class IngredientJson(
    val id: Int,
    val nombre: String,
    val tipo: String,
    val unidad_medida: String
)

@Serializable
data class RecetaIngredienteJson(
    val coctel_id: Int,
    val ingrediente_id: Int,
    val cantidad: Double,
    val unidad: String,
    val opcional: Boolean,
    val notas: String? = null
)

object CocktailJsonParser {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // Caché para evitar leer el JSON múltiples veces
    private var cachedDatabase: CocktailsDatabase? = null

    /**
     * Lee y cachea la base de datos completa del JSON
     */
    private fun loadDatabase(context: Context): CocktailsDatabase {
        // Si ya está en caché, retornar
        cachedDatabase?.let { return it }

        return try {
            val jsonString = context.assets.open("cocktails.json")
                .bufferedReader()
                .use { it.readText() }

            val database = json.decodeFromString<CocktailsDatabase>(jsonString)
            cachedDatabase = database
            database
        } catch (e: IOException) {
            e.printStackTrace()
            throw Exception("Error al leer cocktails.json: ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error al parsear JSON: ${e.message}")
        }
    }

    /**
     * Obtiene los ingredientes de un cóctel específico
     */
    private fun getIngredientesParaCoctel(
        coctelId: Int,
        database: CocktailsDatabase
    ): List<Ingrediente> {
        // Obtener las relaciones de este cóctel
        val relaciones = database.receta_ingredientes.filter { it.coctel_id == coctelId }

        return relaciones.mapNotNull { relacion ->
            // Buscar el ingrediente correspondiente
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
    private fun convertirPasos(pasos: List<String>): List<Paso> {
        return pasos.mapIndexed { index, texto ->
            Paso(n = index + 1, texto = texto)
        }
    }

    /**
     * Lee el archivo cocktails.json y retorna lista de CoctelDetalle
     */
    fun loadCocktailsFromAssets(context: Context): List<CoctelDetalle> {
        return try {
            val database = loadDatabase(context)

            // Convertir cada cocktail del JSON a CoctelDetalle
            database.cocktails.map { cocktailJson ->
                CoctelDetalle(
                    id = cocktailJson.id,
                    nombre = cocktailJson.nombre,
                    descripcion = cocktailJson.descripcion,
                    pasos = convertirPasos(cocktailJson.pasos),
                    ingredientes = getIngredientesParaCoctel(cocktailJson.id, database),
                    utensilios = cocktailJson.utensilios,
                    nivelDificultad = cocktailJson.nivel_dificultad,
                    nivelAlcohol = cocktailJson.nivel_alcohol,
                    saborPredominante = cocktailJson.sabor_predominante,
                    categorias = cocktailJson.categorias,
                    urlVideo = cocktailJson.url_video
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Busca un cóctel específico por ID
     */
    fun getCocktailById(context: Context, id: String): CoctelDetalle? {
        return try {
            val idInt = id.toIntOrNull() ?: return null
            val database = loadDatabase(context)
            val cocktailJson = database.cocktails.find { it.id == idInt } ?: return null

            CoctelDetalle(
                id = cocktailJson.id,
                nombre = cocktailJson.nombre,
                descripcion = cocktailJson.descripcion,
                pasos = convertirPasos(cocktailJson.pasos),
                ingredientes = getIngredientesParaCoctel(cocktailJson.id, database),
                utensilios = cocktailJson.utensilios,
                nivelDificultad = cocktailJson.nivel_dificultad,
                nivelAlcohol = cocktailJson.nivel_alcohol,
                saborPredominante = cocktailJson.sabor_predominante,
                categorias = cocktailJson.categorias,
                urlVideo = cocktailJson.url_video
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Busca cócteles por nombre (búsqueda parcial, case-insensitive)
     */
    fun searchCocktails(context: Context, query: String): List<CoctelDetalle> {
        if (query.isBlank()) return loadCocktailsFromAssets(context)

        return loadCocktailsFromAssets(context).filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.descripcion?.contains(query, ignoreCase = true) == true
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
            e.printStackTrace()
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