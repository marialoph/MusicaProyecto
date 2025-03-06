package com.example.proyectomusica.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val nombre: String,
    val contrasena: String
)
