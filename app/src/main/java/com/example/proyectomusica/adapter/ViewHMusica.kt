package com.example.proyectomusica.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectomusica.databinding.ItemMusicaBinding
import com.example.proyectomusica.models.Musica

class ViewHMusica(private val binding: ItemMusicaBinding,
    private val deleteOnClick: (Int) ->Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun renderize(musica: Musica, position: Int) {
        binding.nameArtista.text = musica.nombre
        binding.generoMusical.text = musica.generoMusical
        binding.numeroAlbums.text = musica.albums
        binding.fechaNacimiento.text = musica.fechaNacimiento
        Glide.with(itemView.context)
            .load(musica.image)
            .centerCrop()
            .into(binding.imagenArtista)

        setOnClickListener(position)
    }
    private fun setOnClickListener(position : Int) {
        binding.buttonBorrar.setOnClickListener {
            deleteOnClick(position)
        }
    }
}