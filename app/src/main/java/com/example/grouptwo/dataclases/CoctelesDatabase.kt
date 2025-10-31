package com.example.grouptwo.dataclases
import kotlinx.serialization.Serializable

@Serializable
data class CoctelesDatabase(
    val schemaVersion: Int,
    val cocteles: List<Coctel>
)
