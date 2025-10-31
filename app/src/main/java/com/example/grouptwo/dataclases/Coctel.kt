package com.example.grouptwo.dataclases

import com.example.grouptwo.dataclases.Ingrediente
import com.example.grouptwo.dataclases.Paso
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
    val categorias: List<Categoria>,
    val imagen: String? = null,
    val ultima_actualizacion: String,
    val url_video_tutorial: String? = null
)