package com.example.proyectomusica.domain.usecase

import com.example.proyectomusica.data.repository.MusicaRepository
import com.example.proyectomusica.domain.models.Musica

class UpdateMusicaUseCase(private val musicaRepository: MusicaRepository) {
    suspend operator fun invoke(pos: Int, musica: Musica): Boolean {
        return musicaRepository.update(pos, musica)
    }
}