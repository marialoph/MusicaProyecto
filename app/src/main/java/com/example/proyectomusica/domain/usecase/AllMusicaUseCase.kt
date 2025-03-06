package com.example.proyectomusica.domain.usecase

import com.example.proyectomusica.data.network.repository.Repository
import com.example.proyectomusica.domain.models.Musica

class AllMusicaUseCase (private  val musicaRepository: Repository) {
     suspend operator fun invoke(): Result<List<Musica>> {
        return musicaRepository.getAllMusica()
    }
}