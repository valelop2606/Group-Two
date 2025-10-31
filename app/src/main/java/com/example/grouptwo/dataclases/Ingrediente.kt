package com.example.grouptwo.dataclases

import kotlinx.serialization.Serializable

@Serializable
data class Ingrediente(
    val nombre: String,
    val cantidad: Double? = null,
    val unidad: String,
    val nota: String? = null
)