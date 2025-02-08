package com.example.proyectomusica.domain.models

class ListMusica private constructor(){
    var musica: MutableList<Musica> = mutableListOf()

    companion object{
        val music : ListMusica by lazy {
            ListMusica()
        }
    }
}