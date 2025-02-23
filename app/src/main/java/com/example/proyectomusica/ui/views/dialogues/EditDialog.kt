package com.example.proyectomusica.ui.views.dialogues

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.proyectomusica.R
import com.example.proyectomusica.databinding.AddEditDeleteBinding
import com.example.proyectomusica.domain.models.Musica
import java.io.ByteArrayOutputStream

class EditDialog(
    private val musica: Musica,
    private val musicaEdit: (Musica) -> Unit
) : DialogFragment() {

    private lateinit var binding: AddEditDeleteBinding

    // Definir los lanzadores de actividades para la cámara y la galería
    private lateinit var activityResultLauncherCamera: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncherGallery: ActivityResultLauncher<Intent>

    private val RESPUESTA_PERMISO_CAMARA = 100

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AddEditDeleteBinding.inflate(LayoutInflater.from(context))

        // Prellenamos los campos con los datos del artista
        binding.editNombre.setText(musica.nombre)
        binding.editGeneroMusical.setText(musica.generoMusical)
        binding.editAlbums.setText(musica.albums)
        binding.editFechaNacimiento.setText(musica.fechaNacimiento)

        // Aquí verifico si el artista tiene una imagen URL y la mostramos
        if (!musica.image.isNullOrBlank()) {
            Glide.with(this)
                .load(musica.image)
                .into(binding.imageView2)

            binding.imageView2.tag = musica.image
        } else {
            binding.imageView2.setImageResource(R.drawable.degradadocardview)
        }

        // Configuramos el botón de cambio de imagen desde la galería
        binding.buttonGaleria.setOnClickListener {
            cambiarImagenGaleria()
        }

        // Configuramos el botón para tomar una foto con la cámara
        binding.buttonCamara.setOnClickListener {
            if (compruebaPermisosCamara()) {
                tomarFotoCamara()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), RESPUESTA_PERMISO_CAMARA)
            }
        }

        // Inicializamos los lanzadores de actividad para cámara y galería
        crearLanzadorActividadCamara()
        crearLanzadorActividadGaleria()

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Editar Artista")
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = binding.editNombre.text.toString()
                val generoMusical = binding.editGeneroMusical.text.toString()
                val albums = binding.editAlbums.text.toString()
                val fechaNacimiento = binding.editFechaNacimiento.text.toString()
                val imagenUrl =  musica.image

                // Verifico si todos los campos están completos
                if (nombre.isNotBlank() && generoMusical.isNotBlank() &&
                    albums.isNotBlank() && fechaNacimiento.isNotBlank()) {
                    // Creamos el objeto Musica editado con la imagen actualizada
                    val musicaEditada = Musica(nombre, generoMusical, albums, fechaNacimiento, imagenUrl)

                    musicaEdit(musicaEditada)

                    // Se vuelve a actualizar la vista con la imagen nueva
                    if (musica.image.isNotBlank()) {
                        Glide.with(this)
                            .load("data:image/jpeg;base64," + musica.image)
                            .into(binding.imageView2)
                    }
                } else {
                    Toast.makeText(requireContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }


            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

    }

    // Método para crear el lanzador de la cámara
    private fun crearLanzadorActividadCamara() {
        activityResultLauncherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val photoBitmap = result.data?.extras?.get("data") as Bitmap
                val base64Image = convertirdorBase64(photoBitmap)

                // Actualiza la vista con Glide
                Glide.with(this)
                    .load("data:image/jpeg;base64,$base64Image")
                    .into(binding.imageView2)  // Actualizo la imagen en el ImageView

                binding.imageView2.tag = "data:image/jpeg;base64,$base64Image"

                saveImageToGallery(photoBitmap)
            }
        }
    }

    //Guardar la imagen en galeria
    private fun saveImageToGallery(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val contentResolver = context?.contentResolver
        val uri = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            context?.contentResolver?.notifyChange(it, null)
        }
    }


    // Método para convertir un bitmap a una cadena base64
    private fun convertirdorBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Método para crear el lanzador de la galería
    private fun crearLanzadorActividadGaleria() {
        activityResultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri = result.data?.data
                Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.degradadocardview)
                    .into(binding.imageView2)
                musica.image = selectedImageUri.toString()
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

    // Método para manejar los permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RESPUESTA_PERMISO_CAMARA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tomarFotoCamara()
                } else {
                    Toast.makeText(requireContext(), "No se ha concedido el permiso para usar la cámara", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Método para cambiar la imagen desde la galería
    private fun cambiarImagenGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncherGallery.launch(intent)
    }
}
