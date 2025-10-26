package com.example.grouptwo.models

import com.google.gson.annotations.SerializedName

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

data class CoctelDetalle(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val pasos: List<String>?,
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
