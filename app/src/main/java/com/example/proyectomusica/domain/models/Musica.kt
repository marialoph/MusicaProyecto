package com.example.proyectomusica.domain.models

import kotlinx.serialization.Serializable

@Serializable
class Musica(
    var nombre: String,
    var generoMusical: String,
    var albums: String,
    var fechaNacimiento: String,
    var image: String? =null
    ) {
        override fun toString(): String {
            return "Musica(nombre='$nombre', género musical='$generoMusical', albums='$albums', fecha nacimiento='$fechaNacimiento', image ='$image')"
        }
    }
