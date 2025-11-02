//package com.example.grouptwo.repository
//
//import android.content.Context
//import androidx.core.content.edit
//
//object Favoritos {
//    private const val PREFS = "favorites_prefs"
//    private const val KEY = "favorite_ids"
//
//    private fun prefs(ctx: Context) =
//        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
//
//    fun getAll(ctx: Context): Set<String> =
//        prefs(ctx).getStringSet(KEY, emptySet()) ?: emptySet()
//
//    fun isFavorite(ctx: Context, id: String): Boolean =
//        getAll(ctx).contains(id)
//
//    fun add(ctx: Context, id: String) {
//        val s = getAll(ctx).toMutableSet()
//        if (s.add(id)) prefs(ctx).edit { putStringSet(KEY, s) }
//    }
//
//    fun remove(ctx: Context, id: String) {
//        val s = getAll(ctx).toMutableSet()
//        if (s.remove(id)) prefs(ctx).edit { putStringSet(KEY, s) }
//    }
//
//    /** Devuelve true si lo dejó en favorito, false si lo quitó */
//    fun toggle(ctx: Context, id: String): Boolean {
//        return if (isFavorite(ctx, id)) { remove(ctx, id); false }
//        else { add(ctx, id); true }
//    }
//}