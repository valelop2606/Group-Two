package com.example.grouptwo.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

// ============ RESPUESTAS DE API (mantener como estaban) ============
data class CoctelResponse(
    val success: Boolean,
    val data: List<Coctel>?,
    val message: String?
)

data class CoctelDetailResponse(
    val success: Boolean,
    val data: CoctelDetalle?,
    val message: String?
)

// ============ MODELO BÁSICO COCTEL (para lista/búsqueda) ============
data class Coctel(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    @SerializedName("nivel_dificultad")
    val nivelDificultad: String?,
    @SerializedName("nivel_alcohol")
    val nivelAlcohol: String?,
    @SerializedName("sabor_predominante")
    val saborPredominante: String?,
    val categorias: List<String>?
)

// ============ MODELO DETALLE COCTEL (ampliado con ingredientes y pasos) ============
data class CoctelDetalle(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val pasos: List<Paso>?,  // ⬅️ Cambiado de List<String> a List<Paso>
    val ingredientes: List<Ingrediente>?,  // ⬅️ AGREGADO
    val utensilios: List<String>?,
    @SerializedName("nivel_dificultad")
    val nivelDificultad: String?,
    @SerializedName("nivel_alcohol")
    val nivelAlcohol: String?,
    @SerializedName("sabor_predominante")
    val saborPredominante: String?,
    val categorias: List<String>?,
    @SerializedName("url_video")
    val urlVideo: String?
)

// ============ CLASES AUXILIARES (nuevas) ============

/**
 * Modelo para Ingrediente
 */
@Serializable
data class Ingrediente(
    val nombre: String,
    val cantidad: Double? = null,
    val unidad: String,
    val nota: String? = null
)

/**
 * Modelo para Paso de preparación
 */
@Serializable
data class Paso(
    val n: Int,
    val texto: String
)

// ============ EXTENSIONES ÚTILES ============

/**
 * Convierte un Coctel básico en CoctelDetalle
 * (útil cuando necesitas más información)
 */
fun Coctel.toDetalle(): CoctelDetalle {
    return CoctelDetalle(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        pasos = emptyList(),
        ingredientes = emptyList(),
        utensilios = emptyList(),
        nivelDificultad = nivelDificultad,
        nivelAlcohol = nivelAlcohol,
        saborPredominante = saborPredominante,
        categorias = categorias,
        urlVideo = null
    )
}

/**
 * Convierte CoctelDetalle a Coctel básico
 */
fun CoctelDetalle.toCoctel(): Coctel {
    return Coctel(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        nivelDificultad = nivelDificultad,
        nivelAlcohol = nivelAlcohol,
        saborPredominante = saborPredominante,
        categorias = categorias
    )
}