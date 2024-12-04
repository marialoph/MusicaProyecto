package com.example.proyectomusica.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectomusica.databinding.ItemMusicaBinding
import com.example.proyectomusica.models.Musica

//Esta clase se encarga de representar cada ítem
class ViewHMusica(private val binding: ItemMusicaBinding,
    private val deleteOnClick: (Int) ->Unit,
    private val editOnClick : (Musica) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    //Método para mostrar los datos del objeto Musica
    fun renderize(musica: Musica) {
        binding.nameArtista.text = musica.nombre
        binding.generoMusical.text = musica.generoMusical
        binding.numeroAlbums.text = musica.albums
        binding.fechaNacimiento.text = musica.fechaNacimiento
        Glide.with(itemView.context)
            .load(musica.image)
            .centerCrop()
            .into(binding.imagenArtista)

        setOnClickListener(musica)
    }

    // Método para que al pulsar el boton se elimine el artista y que al pulsar editar me salga el dialogo
    private fun setOnClickListener(musica: Musica) {
        binding.buttonBorrar.setOnClickListener {
            deleteOnClick(adapterPosition)
        }

        binding.buttonEditar.setOnClickListener{
            editOnClick(musica)
        }
    }
}