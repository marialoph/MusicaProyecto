package com.example.proyectomusica.domain.repository

import com.example.proyectomusica.domain.models.Musica

interface InterfaceDao {
    fun getDataMusica(): List<Musica>

    suspend fun deleteMusica(id:Int) : Boolean

    suspend fun addMusica(musica: Musica) : Musica?

    suspend fun update(id: Int, musica: Musica) : Boolean

    suspend fun exisMusica(musica: Musica) : Boolean

    fun getMusicaByPos(pos:Int) : Musica?
}