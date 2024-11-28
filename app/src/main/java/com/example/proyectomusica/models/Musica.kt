package com.example.proyectomusica.models


class Musica (
    var nombre: String,
    var generoMusical: String,
    var albums: String,
    var fechaNacimiento : String,
    var image : String
    ) {
        override fun toString(): String {
            return "Musica(nombre='$nombre', género musical='$generoMusical', albums='$albums', fecha nacimiento='$fechaNacimiento', image ='$image')"
        }
    }
