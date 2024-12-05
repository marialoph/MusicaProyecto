package com.example.proyectomusica.dialogues

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

//Crea un dialogo que permite borrar el artista seleccionado
class DeleteDialog(
    private val artista: String,
    private val onConfirm: () -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Eliminar artista")
            .setMessage("¿Desea borrar este artista: $artista?")
            .setPositiveButton("Sí") { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}
