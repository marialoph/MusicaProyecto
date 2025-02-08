package com.example.proyectomusica.domain.usecase

import com.example.proyectomusica.data.repository.MusicaRepository
import com.example.proyectomusica.domain.models.Musica

class NewMusicaUseCase(private val musicaRepository: MusicaRepository) {
    suspend operator fun  invoke(newMusica: Musica) : Musica?{
        return if (!musicaRepository.exisMusica(newMusica)){
            return musicaRepository.addMusica(newMusica)
        }else{
            null
        }
    }
}