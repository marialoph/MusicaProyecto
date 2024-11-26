package com.example.proyectomusica.dao

import com.example.proyectomusica.interfaces.InterfaceMusica
import com.example.proyectomusica.models.Musica
import com.example.proyectomusica.objects_models.Repository


class DaoMusica private constructor(): InterfaceMusica {
        companion object {
            val myDao: DaoMusica by lazy {
                DaoMusica()
            }
        }
    override fun getDataMusica(): List<Musica> = Repository.listaMusica
}
