package com.example.proyectomusica.domain.usecase

import com.example.proyectomusica.data.network.repository.Repository
import com.example.proyectomusica.domain.models.Musica

class UpdateMusicaUseCase(private val musicaRepository: Repository) {
    suspend operator fun invoke(nombreMusica: String, musica: Musica): Result<Unit> {
        return musicaRepository.updateMusica(musica, nombreMusica)
    }
}
