package com.example.grouptwo.repository

import android.content.Context
import com.example.grouptwo.models.CoctelDetalle
import com.example.grouptwo.utils.CocktailJsonParser

/**
 * Objeto companion para mantener compatibilidad con código existente
 * que usa CoctelRepositori.getAll() y CoctelRepositori.getById()
 */
object CoctelRepositori {

    /**
     * Obtiene todos los cócteles desde el JSON
     */
    fun getAll(context: Context): List<CoctelDetalle> {
        return CocktailJsonParser.loadCocktailsFromAssets(context)
    }

    /**
     * Obtiene un cóctel específico por ID
     */
    fun getById(context: Context, id: String): CoctelDetalle? {
        return CocktailJsonParser.getCocktailById(context, id)
    }

    /**
     * Busca cócteles por nombre
     */
    fun search(context: Context, query: String): List<CoctelDetalle> {
        return CocktailJsonParser.searchCocktails(context, query)
    }
}
