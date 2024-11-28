package com.example.proyectomusica.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomusica.databinding.ItemMusicaBinding
import com.example.proyectomusica.models.Musica

class AdapterMusica(var listaMusica : MutableList<Musica>,
    var deleteOnClick: (Int) -> Unit
) : RecyclerView.Adapter<ViewHMusica>(){

    //Método llamado por el recyclerview para crear un nuevo viewHolder
    //Retorna la vista inflada del diseño de los item y asocia la acción del boton eliminar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHMusica {
        val binding = ItemMusicaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHMusica(binding, deleteOnClick)
    }

    //Este método renderiza los datos de cada objeto Musica en el ViewHolder
    override fun onBindViewHolder(holder: ViewHMusica, position: Int) {
        holder.renderize(listaMusica[position])
    }

    //Devuelve el número de elementos en la lista
    override fun getItemCount(): Int = listaMusica.size

}