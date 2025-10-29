package com.example.grouptwo

import kotlinx.serialization.Serializable

@Serializable
data class Coctel(
    val id: String,
    val nombre: String,
    val descripcion: String? = null,
    val dificultad: String,
    val nivel_alcohol: String,
    val sabor: String,
    val ingredientes: List<Ingrediente>,
    val pasos: List<Paso>,
    val imagenes: List<String> = emptyList(),
    val ultima_actualizacion: String,
    val url_video_tutorial : String
)