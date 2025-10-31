package com.example.grouptwo.dataclases

import kotlinx.serialization.Serializable

@Serializable
data class Paso(
    val n: Int,
    val texto: String
)