package com.example.proyectomusica.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val nombre: String,
    val contrasena: String
)
