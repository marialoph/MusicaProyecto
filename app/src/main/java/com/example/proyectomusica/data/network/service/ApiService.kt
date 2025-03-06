package com.example.proyectomusica.data.network.service

import com.example.proyectomusica.data.network.models.AuthResponse
import com.example.proyectomusica.data.network.models.LoginRequest
import com.example.proyectomusica.data.network.models.RegisterRequest
import com.example.proyectomusica.domain.models.Musica
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

import retrofit2.Response

interface ApiService {
    @POST("/auth")
    suspend fun loginUser(@Body user: LoginRequest): Response<AuthResponse>

    @POST("/register")
    suspend fun registerUser(@Body user: RegisterRequest): Response<Unit>

    @GET("/musica")
    suspend fun getAllMusica(@Header("Authorization") token: String): List<Musica>

    @POST("/musica")
    suspend fun addMusica(@Header("Authorization") token: String, @Body musica: Musica): Response<Unit>

    @PATCH("/musica/{nombreMusica}")
    suspend fun updateMusica(
        @Header("Authorization") token: String,
        @Body musica: Musica,
        @Path("nombreMusica") nombreMusica: String
    ): Response<Unit>

    @DELETE("/musica/{nombreMusica}")
    suspend fun deleteMusica(@Header("Authorization") token: String, @Path("nombreMusica") nombreMusica: String): Response<Unit>
}
