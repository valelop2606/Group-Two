package com.example.grouptwo.repository

import android.content.Context
import android.content.SharedPreferences

object Favoritos {
    private const val PREFS = "favoritos_prefs"
    private const val KEY_SET = "ids_favoritos"

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    private fun getSet(ctx: Context): MutableSet<String> =
        prefs(ctx).getStringSet(KEY_SET, emptySet())?.toMutableSet() ?: mutableSetOf()

    private fun save(ctx: Context, set: Set<String>) {
        // Guardar SIEMPRE una nueva instancia del set
        prefs(ctx).edit().putStringSet(KEY_SET, set.toMutableSet()).apply()
    }

    /** Devuelve true si quedó añadido, false si quedó removido */
    fun toggle(ctx: Context, id: String): Boolean {
        val s = getSet(ctx)
        val added = if (s.contains(id)) { s.remove(id); false } else { s.add(id); true }
        save(ctx, s)
        return added
    }

    fun add(ctx: Context, id: String) {
        val s = getSet(ctx)
        if (s.add(id)) save(ctx, s)
    }

    fun remove(ctx: Context, id: String) {
        val s = getSet(ctx)
        if (s.remove(id)) save(ctx, s)
    }

    fun isFavorite(ctx: Context, id: String): Boolean = getSet(ctx).contains(id)

    fun all(ctx: Context): Set<String> = getSet(ctx)
}
