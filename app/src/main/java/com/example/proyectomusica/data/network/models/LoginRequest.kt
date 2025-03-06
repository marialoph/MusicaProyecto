package com.example.proyectomusica.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val nombre: String,
    val contrasena: String
)
