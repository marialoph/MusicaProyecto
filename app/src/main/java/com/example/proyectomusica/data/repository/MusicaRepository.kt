package com.example.proyectomusica.data.repository

import com.example.proyectomusica.data.datasource.Repository
import com.example.proyectomusica.domain.models.ListMusica
import com.example.proyectomusica.domain.models.Musica
import com.example.proyectomusica.domain.repository.InterfaceDao

class MusicaRepository() : InterfaceDao {
    var musicaList: MutableList<Musica> = mutableListOf()

    //Método que devuelve la lista de musica
    override fun getDataMusica(): MutableList<Musica> {
        return Repository.listaMusica.toMutableList()
    }

    //Método que elimina un objeto(musica) de la lista por su posición
    override suspend fun deleteMusica(pos: Int): Boolean {
        return if (pos >= 0 && pos < musicaList.size) {
            musicaList.removeAt(pos)
            true
        } else {
            false
        }
    }

    //Método que añade al repositorio un objeto(musica) nuevo
    override suspend fun addMusica(musica: Musica) : Musica?{
        ListMusica.music.musica.add(musica)
        return musica
    }

    //Método que actualiza un objeto(musica) en la posicion en la que esta
    override suspend fun update(pos: Int, musica: Musica): Boolean {
        if (pos >= 0 && pos < musicaList.size) {
            musicaList[pos] = musica
            return true
        }
        return false
    }

    //Método que verifica si el objeto(musica) existe
    override suspend fun exisMusica(musica: Musica) : Boolean = ListMusica.music.musica.contains(musica)


    //Devuelve el objeto(musica) en una posición específica
    override fun getMusicaByPos (pos:Int) : Musica? {
        return if (pos < ListMusica.music.musica.size)
            ListMusica.music.musica.get(pos)
        else
            null
    }
}