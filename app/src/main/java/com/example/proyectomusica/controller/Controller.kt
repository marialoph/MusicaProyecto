package com.example.proyectomusica.controller

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.example.proyectomusica.MainActivity
import com.example.proyectomusica.adapter.AdapterMusica
import com.example.proyectomusica.dao.DaoMusica
import com.example.proyectomusica.databinding.ActivityMainBinding
import com.example.proyectomusica.databinding.ItemMusicaBinding
import com.example.proyectomusica.models.Musica

class Controller (val context : Context, var binding : ActivityMainBinding){
    lateinit var listaMusica : MutableList<Musica> //lista de objetos

    init {
        initData()
    }
    fun initData(){

        listaMusica = DaoMusica. myDao.getDataMusica(). toMutableList()
    }

    fun loggOut() {
        Toast.makeText( context, "He mostrado los datos en pantalla", Toast. LENGTH_LONG).show()
        listaMusica.forEach{
            println (it)
        }
    }

    fun setAdapter() {
        val myActivity = context as MainActivity

        val adapterMusica = AdapterMusica(
            listaMusica,
            {pos -> delMusica(pos)},

        )
        myActivity. binding.myRecyclerView.adapter = adapterMusica
    }

    // Elimina la música de la lista
   fun delMusica(pos: Int) {
        listaMusica.removeAt(pos)
        (binding.myRecyclerView.adapter as AdapterMusica).notifyItemRemoved(pos)
        Toast.makeText(context, "Se eliminó el artista de la posición $pos", Toast.LENGTH_SHORT).show()
    }



}