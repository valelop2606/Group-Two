package com.example.grouptwo.repository

import android.content.Context
import com.example.grouptwo.models.CoctelDetalle
import com.example.grouptwo.utils.CocktailJsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoctelRepository(private val context: Context) {

    /**
     * Obtiene todos los cócteles del archivo JSON
     */
    suspend fun obtenerTodosCocteles(): Result<List<CoctelDetalle>> {
        return withContext(Dispatchers.IO) {
            try {
                val cocktails = CocktailJsonParser.loadCocktailsFromAssets(context)
                if (cocktails.isNotEmpty()) {
                    Result.success(cocktails)
                } else {
                    Result.failure(Exception("No se encontraron cócteles"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Obtiene un cóctel específico por su ID
     */
    suspend fun obtenerCoctelPorId(id: String): Result<CoctelDetalle> {
        return withContext(Dispatchers.IO) {
            try {
                val cocktail = CocktailJsonParser.getCocktailById(context, id)
                if (cocktail != null) {
                    Result.success(cocktail)
                } else {
                    Result.failure(Exception("Cóctel no encontrado"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Busca cócteles por nombre o descripción
     */
    suspend fun buscarCocteles(query: String): Result<List<CoctelDetalle>> {
        return withContext(Dispatchers.IO) {
            try {
                val results = CocktailJsonParser.searchCocktails(context, query)
                Result.success(results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Obtiene cócteles filtrados por categoría
     */
    suspend fun obtenerCoctelesPorCategoria(categoria: String): Result<List<CoctelDetalle>> {
        return withContext(Dispatchers.IO) {
            try {
                val cocktails = CocktailJsonParser.getCocktailsByCategory(context, categoria)
                Result.success(cocktails)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Versión sincrónica para compatibilidad con código existente
     */
    fun getAll(): List<CoctelDetalle> {
        return CocktailJsonParser.loadCocktailsFromAssets(context)
    }

    /**
     * Obtiene un cóctel por ID (versión sincrónica)
     */
    fun getById(id: String): CoctelDetalle? {
        return CocktailJsonParser.getCocktailById(context, id)
    }
    // Añadir estos métodos al final de CoctelRepository:

    // ✅ Búsqueda síncrona para MainActivity
    fun search(query: String): List<CoctelDetalle> {
        return CocktailJsonParser.searchCocktails(context, query)
    }

    // ✅ Categorías síncrona
    fun getByCategory(category: String): List<CoctelDetalle> {
        return CocktailJsonParser.getCocktailsByCategory(context, category)
    }

}