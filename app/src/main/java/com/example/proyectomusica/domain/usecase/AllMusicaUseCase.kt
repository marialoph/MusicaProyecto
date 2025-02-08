package com.example.proyectomusica.domain.usecase

import com.example.proyectomusica.data.repository.MusicaRepository
import com.example.proyectomusica.domain.models.Musica

class AllMusicaUseCase (private  val musicaRepository: MusicaRepository) {
     operator fun invoke(): MutableList<Musica> {
        return musicaRepository.getDataMusica()
    }
}