package com.example.proyectomusica.domain.usecase

import com.example.proyectomusica.data.repository.MusicaRepository

class DeleteMusicaUseCase(private val musicaRepository: MusicaRepository) {
    suspend operator fun invoke(pos: Int): Boolean {
        return musicaRepository.deleteMusica(pos)
    }
}