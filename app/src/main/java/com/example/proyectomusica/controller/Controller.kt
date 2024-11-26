package com.example.proyectomusica.controller

import android.content.Context
import android.widget.Toast
import com.example.proyectomusica.MainActivity
import com.example.proyectomusica.adapter.AdapterMusica
import com.example.proyectomusica.dao.DaoMusica
import com.example.proyectomusica.models.Musica

class Controller (val context : Context){
    lateinit var listaMusica : MutableList<Musica> //lista de objetos
    init {
        initData()
    }
    fun initData(){
        // listHotels = DaoHotels2.myDao.toMutableList()
        listaMusica = DaoMusica. myDao.getDataMusica(). toMutableList() //llamamos al singleton.
    }
    fun loggOut() {
        Toast.makeText( context, "He mostrado los datos en pantalla", Toast. LENGTH_LONG).show()
        listaMusica.forEach{
            println (it)
        }
    }
    fun setAdapter() { // Cargamos nuestro AdapterHotgel al adapter del RecyclerView
        val myActivity = context as MainActivity
        myActivity. binding.myRecyclerView.adapter = AdapterMusica(listaMusica) // Cargamos el Adapter que creamos.
    }
}