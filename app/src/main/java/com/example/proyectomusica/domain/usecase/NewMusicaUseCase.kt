package com.example.proyectomusica.domain.usecase

import com.example.proyectomusica.data.network.repository.Repository
import com.example.proyectomusica.domain.models.Musica

class NewMusicaUseCase(private val musicaRepository: Repository) {
    suspend operator fun invoke(newMusica: Musica): Result<Unit> {
        return musicaRepository.addMusica(newMusica)
    }
}
