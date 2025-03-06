package com.example.proyectomusica.domain.usecase

import com.example.proyectomusica.data.network.repository.Repository

class LoginUseCase(private val repository: Repository) {
    suspend fun login(nombre: String, contrasena: String): Result<String> {
        return repository.loginUser(nombre, contrasena)
    }
}
