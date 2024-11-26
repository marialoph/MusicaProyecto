package com.example.proyectomusica.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectomusica.databinding.ItemMusicaBinding
import com.example.proyectomusica.models.Musica

class ViewHMusica(private val binding: ItemMusicaBinding) : RecyclerView.ViewHolder(binding.root) {
    fun renderize(musica: Musica) {
        binding.nameArtista.text = musica.nombre
        binding.generoMusical.text = musica.generoMusical
        binding.numeroAlbums.text = musica.albums
        binding.fechaNacimiento.text = musica.fechaNacimiento
        Glide.with(itemView.context)
            .load(musica.image)
            .centerCrop()
            .into(binding.imagenArtista)
    }
}