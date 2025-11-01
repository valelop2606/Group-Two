package com.example.grouptwo.repository

import android.content.Context


object GuardarPerfil {
    private const val PREFS = "perfil_prefs"
    private const val K_NOMBRE = "nombre_usuario"
    private const val K_DESC = "descripcion_usuario"

    fun save(context: Context, nombre: String?, descripcion: String?) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(K_NOMBRE, nombre)
            .putString(K_DESC, descripcion)
            .apply()
    }

    fun loadNombre(context: Context): String? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(K_NOMBRE, null)

    fun loadDescripcion(context: Context): String? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(K_DESC, null)
}
