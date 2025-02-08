package com.example.proyectomusica.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomusica.data.repository.MusicaRepository
import com.example.proyectomusica.domain.models.Musica
import com.example.proyectomusica.domain.usecase.AllMusicaUseCase
import com.example.proyectomusica.domain.usecase.DeleteMusicaUseCase
import com.example.proyectomusica.domain.usecase.NewMusicaUseCase
import com.example.proyectomusica.domain.usecase.UpdateMusicaUseCase
import kotlinx.coroutines.launch

class MusicaViewModel () : ViewModel() {
    //Inicializo el repositorio y los casos de uso
    private val musicaRepository = MusicaRepository()
    private val getAllMusicaUseCase = AllMusicaUseCase(musicaRepository)
    private val newMusicaUseCase = NewMusicaUseCase(musicaRepository)
    private val updateMusicaUseCase = UpdateMusicaUseCase(musicaRepository)
    private val deleteMusicaUseCase = DeleteMusicaUseCase(musicaRepository)
    val musicaListData = MutableLiveData<List<Musica>>()


    //Método para obtener y mostrar toda la lista de musica
    fun showMusica() {
        viewModelScope.launch {
            val data: List<Musica> = getAllMusicaUseCase()
            musicaListData.postValue(data)
        }
    }


    //Método que añade una nueva música
    //Llamo al caso de uso para agregar la nueva musica
    //Por último actualizo la lista
    fun addMusica(musica: Musica) {
        viewModelScope.launch {
            val newMusica = newMusicaUseCase(musica)
            newMusica?.let {
                val updatedList = musicaListData.value?.toMutableList() ?: mutableListOf()
                updatedList.add(it)
                musicaListData.postValue(updatedList)
            }
        }
    }

    //Método para actualizar una música de la lista
    //Si la posición es válida se actualiza la música en esa posición.
    //Se actualiza la lista
    fun updateMusica(musica: Musica, pos: Int) {
        viewModelScope.launch {
            val updatedList = musicaListData.value?.toMutableList() ?: mutableListOf()

            if (pos >= 0 && pos < updatedList.size) {
                updatedList[pos] = musica
                musicaListData.postValue(updatedList)
                updateMusicaUseCase(pos, musica)
            }
        }
    }


    //Método para eliminar una musica de la lista
    //Si la posición es válida se elimina la música en esa posición
    //Se actualiza la lista
    fun deleteMusica(pos: Int) {
        viewModelScope.launch {
            val updatedList = musicaListData.value?.toMutableList() ?: mutableListOf()
            if (pos >= 0 && pos < updatedList.size) {
                updatedList.removeAt(pos)
                musicaListData.postValue(updatedList)
                deleteMusicaUseCase(pos)
            }
        }
    }


}


