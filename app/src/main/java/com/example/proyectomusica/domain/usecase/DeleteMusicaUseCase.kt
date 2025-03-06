package com.example.proyectomusica.domain.usecase

import com.example.proyectomusica.data.network.repository.Repository

class DeleteMusicaUseCase(private val musicaRepository: Repository) {
    suspend operator fun invoke(nombreMusica: String): Result<Unit> {
        return musicaRepository.deleteMusica(nombreMusica)
    }
}