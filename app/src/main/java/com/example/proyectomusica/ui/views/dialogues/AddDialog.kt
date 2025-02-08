package com.example.proyectomusica.ui.views.dialogues

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.proyectomusica.databinding.AddEditDeleteBinding
import com.example.proyectomusica.domain.models.Musica

//Crea un dialogo que permite al usuario añadir un nuevo cardview de un artista
class AddDialog(private val musicaAdd : (Musica) -> Unit) : DialogFragment() {
    lateinit var binding: AddEditDeleteBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AddEditDeleteBinding.inflate(LayoutInflater.from(context))
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Añadir Nuevo Artista")
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = binding.editNombre.text.toString()
                val generoMusical = binding.editGeneroMusical.text.toString()
                val albums = binding.editAlbums.text.toString()
                val fechaNacimiento = binding.editFechaNacimiento.text.toString()
                val imagenUrl = binding.editImagen.text.toString()

                if (nombre.isNotBlank() && generoMusical.isNotBlank() &&
                    albums.isNotBlank() && fechaNacimiento.isNotBlank() && imagenUrl.isNotBlank()
                ) {
                    val nuevaMusica =
                        Musica(nombre, generoMusical, albums, fechaNacimiento, imagenUrl)
                    musicaAdd(nuevaMusica)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Por favor llena todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
    }
}

