package com.example.proyectomusica.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int? = null,
    val nombre: String,
    val contrasena: String,
    val token: String? = null
)
