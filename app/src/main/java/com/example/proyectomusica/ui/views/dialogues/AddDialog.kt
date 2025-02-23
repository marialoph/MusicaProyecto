package com.example.proyectomusica.ui.views.dialogues

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.proyectomusica.databinding.AddEditDeleteBinding
import com.example.proyectomusica.domain.models.Musica
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.proyectomusica.R
import java.io.ByteArrayOutputStream

//Crea un dialogo que permite al usuario añadir un nuevo cardview de un artista
class AddDialog(private val musicaAdd: (Musica) -> Unit) : DialogFragment() {

    lateinit var binding: AddEditDeleteBinding

    // Definir los lanzadores de actividades para la cámara y la galería
    private lateinit var activityResultLauncherCamera: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncherGallery: ActivityResultLauncher<Intent>

    private val RESPUESTA_PERMISO_CAMARA = 100

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AddEditDeleteBinding.inflate(LayoutInflater.from(context))

        // Inicializo los lanzadores de actividad para cámara y galería
        crearLanzadorActividadCamara()
        crearLanzadorActividadGaleria()

        // Configuro el botón de cambio de imagen desde la galería
        binding.buttonGaleria.setOnClickListener {
            cambiarImagenGaleria()
        }

        // Configuro el botón para tomar una foto con la cámara
        binding.buttonCamara.setOnClickListener {
            if (compruebaPermisosCamara()) {
                tomarFotoCamara()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), RESPUESTA_PERMISO_CAMARA)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Añadir Nuevo Artista")
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = binding.editNombre.text.toString()
                val generoMusical = binding.editGeneroMusical.text.toString()
                val albums = binding.editAlbums.text.toString()
                val fechaNacimiento = binding.editFechaNacimiento.text.toString()
                val imagenUrl = binding.imageView2.tag.toString()

                if (nombre.isNotBlank() && generoMusical.isNotBlank() &&
                    albums.isNotBlank() && fechaNacimiento.isNotBlank() && imagenUrl.isNotBlank()) {
                    val nuevaMusica = Musica(nombre, generoMusical, albums, fechaNacimiento, imagenUrl)
                    musicaAdd(nuevaMusica)
                } else {
                    Toast.makeText(requireContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
    }

    // Método para crear el lanzador de la cámara
    private fun crearLanzadorActividadCamara() {
        activityResultLauncherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val photoBitmap = result.data?.extras?.get("data") as Bitmap
                val base64Image = convertidorBase64(photoBitmap)

                // Actualiza la vista con Glide
                Glide.with(this)
                    .load("data:image/jpeg;base64,$base64Image")
                    .into(binding.imageView2)  // Actualiza la imagen en el ImageView

                binding.imageView2.tag = "data:image/jpeg;base64,$base64Image"
            }
        }
    }

    // Método para crear el lanzador de la galería
    private fun crearLanzadorActividadGaleria() {
        activityResultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri = result.data?.data

                // Muestra la imagen seleccionada en el ImageView
                Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.degradadocardview)
                    .into(binding.imageView2)

                binding.imageView2.tag = selectedImageUri.toString()
            }
        }
    }

    // Método para verificar si tenemos permisos de cámara
    private fun compruebaPermisosCamara(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    // Método para tomar una foto con la cámara
    private fun tomarFotoCamara() {
        val intentCamara = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activityResultLauncherCamera.launch(intentCamara)
    }

    // Método para cambiar la imagen desde la galería
    private fun cambiarImagenGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncherGallery.launch(intent)
    }

    // Método para convertir un bitmap a una cadena base64
    private fun convertidorBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
