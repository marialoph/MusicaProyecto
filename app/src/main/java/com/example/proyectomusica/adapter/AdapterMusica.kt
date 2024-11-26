package com.example.proyectomusica.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomusica.databinding.ItemMusicaBinding
import com.example.proyectomusica.models.Musica

class AdapterMusica(var listaMusica : MutableList<Musica>) : RecyclerView.Adapter<ViewHMusica>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHMusica {
        val binding = ItemMusicaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHMusica(binding)
    }

    /*
    Este método renderiza los datos de cada objeto Musica en el ViewHolder
    */
    override fun onBindViewHolder(holder: ViewHMusica, position: Int) {
        holder.renderize(listaMusica[position]) // Renderizamos la vista con los datos
    }

    /*
    Devuelve el número de elementos en la lista
    */
    override fun getItemCount(): Int = listaMusica.size
}