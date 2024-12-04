package com.example.proyectomusica.dialogues

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.proyectomusica.databinding.AddEditDeleteBinding
import com.example.proyectomusica.models.Musica


//Crea un dialogo que permita modificar el artista que se ha seleccionado
class EditDialog(
    private val musica: Musica,
    private val musicaEdit: (Musica) -> Unit) : DialogFragment() {
    private lateinit var binding: AddEditDeleteBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AddEditDeleteBinding.inflate(LayoutInflater.from(context))

        // Prellena los campos con los datos del artista
        binding.editNombre.setText(musica.nombre)
        binding.editGeneroMusical.setText(musica.generoMusical)
        binding.editAlbums.setText(musica.albums)
        binding.editFechaNacimiento.setText(musica.fechaNacimiento)
        binding.editImagen.setText(musica.image)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Editar Artista")
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = binding.editNombre.text.toString()
                val generoMusical = binding.editGeneroMusical.text.toString()
                val albums = binding.editAlbums.text.toString()
                val fechaNacimiento = binding.editFechaNacimiento.text.toString()
                val imagenUrl = binding.editImagen.text.toString()

                if (nombre.isNotBlank() && generoMusical.isNotBlank() &&
                    albums.isNotBlank() && fechaNacimiento.isNotBlank() && imagenUrl.isNotBlank()
                ) {

                    val musicaEditada = Musica(nombre, generoMusical, albums, fechaNacimiento, imagenUrl)
                    musicaEdit(musicaEditada)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Por favor llena todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

}