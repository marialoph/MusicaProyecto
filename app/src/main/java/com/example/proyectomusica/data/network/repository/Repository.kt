package com.example.proyectomusica.data.network.repository

import android.content.Context
import com.example.proyectomusica.data.network.models.LoginRequest
import com.example.proyectomusica.data.network.models.RegisterRequest

import com.example.proyectomusica.data.network.service.ApiService
import com.example.proyectomusica.domain.models.Musica


class Repository(private val apiService: ApiService, private val context: Context) {

    // Métodos de autenticación
    suspend fun loginUser(nombre: String, contrasena: String): Result<String> {
        return try {
            val response = apiService.loginUser(LoginRequest(nombre, contrasena))
            if (response.isSuccessful) {
                val body = response.body()
                val token = body?.token ?: return Result.failure(Exception("Token no recibido"))
                saveToken(token)
                Result.success(token)
            } else {
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Método para registrar un nuevo usuario
    suspend fun registerUser(nombre: String, contrasena: String): Result<String> {
        return try {
            val response = apiService.registerUser(RegisterRequest(nombre, contrasena))
            if (response.isSuccessful) {
                Result.success("Usuario registrado con éxito")
            } else {
                Result.failure(Exception("Error en el registro"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Método para obtener todo_ el listado de mucisa
    suspend fun getAllMusica(): Result<List<Musica>> {
        return try {
            val token = getToken()
            if (token.isEmpty()) return Result.failure(Exception("Token no encontrado"))
            val response = apiService.getAllMusica("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Método para añadir musica
    suspend fun addMusica(musica: Musica): Result<Unit> {
        return try {
            val token = getToken()
            if (token.isEmpty()) return Result.failure(Exception("Token no encontrado"))
            val response = apiService.addMusica("Bearer $token", musica)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al agregar la música"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Método para actualizar musica
    suspend fun updateMusica(musica: Musica, nombreMusica: String): Result<Unit> {
        return try {
            val token = getToken()
            if (token.isEmpty()) return Result.failure(Exception("Token no encontrado"))
            val response = apiService.updateMusica("Bearer $token", musica, nombreMusica)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al actualizar la música"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Método para eliminar musica
    suspend fun deleteMusica(nombreMusica: String): Result<Unit> {
        return try {
            val token = getToken()
            if (token.isEmpty()) return Result.failure(Exception("Token no encontrado"))
            val response = apiService.deleteMusica("Bearer $token", nombreMusica)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar la música"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Estos dos métodos para manejar el token
    fun saveToken(token: String) {
        val prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        prefs.edit().putString("TOKEN", token).apply()
    }

    private fun getToken(): String {
        val prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        return prefs.getString("TOKEN", "") ?: ""
    }
}
