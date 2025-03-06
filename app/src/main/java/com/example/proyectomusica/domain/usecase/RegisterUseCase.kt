package com.example.proyectomusica.domain.usecase

import com.example.proyectomusica.data.network.repository.Repository

class RegisterUseCase(private val repository: Repository) {
    suspend fun register(nombre: String, contrasena: String): Result<String> {
        return repository.registerUser(nombre, contrasena)
    }
}
